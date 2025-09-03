package ru.ed.module.front.views.characters;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import ru.ed.module.back.model.PoiCharacter;
import ru.ed.module.back.model.Spec;
import ru.ed.module.back.service.PoiCharacterService;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Персонажи")
@Route("characters")
@Menu(order = 1, icon = LineAwesomeIconUrl.ANDROID)
@Uses(Icon.class)
public class CharactersView extends Div {

    private Grid<PoiCharacter> grid;

    private Filters filters;
    private final PoiCharacterService poiCharacterService;

    public CharactersView(PoiCharacterService poiCharacterService) {
        this.poiCharacterService = poiCharacterService;
        setSizeFull();
        addClassNames("characters-view");

        filters = new Filters(this::refreshGrid);
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private HorizontalLayout createMobileFilters() {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
            LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filters.getClassNames().contains("visible")) {
                filters.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filters.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
    }

    public static class Filters extends Div implements Specification<PoiCharacter> {

        private final TextField name = new TextField("Имя");
        private final MultiSelectComboBox<Spec> specs = new MultiSelectComboBox<>("Класс");

        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                LumoUtility.BoxSizing.BORDER);
            name.setPlaceholder("Имя персонажа");
            specs.setItems(Spec.values());

            // Action buttons
            Button resetBtn = new Button("Сброс");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                name.clear();
                specs.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Поиск");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(name, specs, actions);
        }

        @Override
        public Predicate toPredicate(Root<PoiCharacter> root,
                                     CriteriaQuery<?> query,
                                     CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!name.isEmpty()) {
                String lowerCaseFilter = name.getValue().toLowerCase();
                Predicate nameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    lowerCaseFilter + "%");
                predicates.add(criteriaBuilder.or(nameMatch));
            }

            if (!specs.isEmpty()) {
                String databaseColumn = "spec";
                List<Predicate> specPredicates = new ArrayList<>();
                for (Spec spec : specs.getValue()) {
                    specPredicates
                        .add(criteriaBuilder.equal(criteriaBuilder.literal(spec), root.get(databaseColumn)));
                }
                predicates.add(criteriaBuilder.or(specPredicates.toArray(Predicate[]::new)));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }

        private String ignoreCharacters(String characters, String in) {
            String result = in;
            for (int i = 0; i < characters.length(); i++) {
                result = result.replace("" + characters.charAt(i), "");
            }
            return result;
        }

        private Expression<String> ignoreCharacters(String characters, CriteriaBuilder criteriaBuilder,
                                                    Expression<String> inExpression) {
            Expression<String> expression = inExpression;
            for (int i = 0; i < characters.length(); i++) {
                expression = criteriaBuilder.function("replace", String.class, expression,
                    criteriaBuilder.literal(characters.charAt(i)), criteriaBuilder.literal(""));
            }
            return expression;
        }

    }

    private Component createGrid() {
        grid = new Grid<>(PoiCharacter.class, false);
        grid.addColumn("poiId").setAutoWidth(true);
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("bm").setAutoWidth(true);
        grid.addColumn("multiplier").setAutoWidth(true);
        grid.addColumn("spec").setAutoWidth(true);

        grid.setItems(query -> poiCharacterService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
            .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}

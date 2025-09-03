package ru.ed.module.front.views.participants;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import ru.ed.module.back.model.Participant;
import ru.ed.module.back.service.ParticipantService;

@PageTitle("Участники")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.ACCESSIBLE_ICON)
@Uses(Icon.class)
public class ParticipantsView extends Div {

    private Grid<Participant> grid;

    private Filters filters;
    private final ParticipantService participantService;

    public ParticipantsView(ParticipantService participantService) {
        this.participantService = participantService;
        setSizeFull();
        addClassNames("participants-view");

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

    public static class Filters extends Div implements Specification<Participant> {

        private final TextField name = new TextField("Имя");
        private final TextField telegramId = new TextField("ID в tg");

        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            name.setPlaceholder("Имя пользователя");
            telegramId.setPlaceholder("ИД в телегам");

            // Action buttons
            Button resetBtn = new Button("Сбросить");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                name.clear();
                telegramId.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Поиск");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(name, telegramId, actions);
        }

        @Override
        public Predicate toPredicate(Root<Participant> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!name.isEmpty()) {
                String lowerCaseFilter = name.getValue().toLowerCase();
                Predicate usernameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
                        lowerCaseFilter + "%");
                Predicate alternativeMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("alternative")),
                        lowerCaseFilter + "%");
                predicates.add(criteriaBuilder.or(usernameMatch, alternativeMatch));
            }
            if (!telegramId.isEmpty()) {
                String databaseColumn = "telegramId";

                String lowerCaseFilter = telegramId.getValue().toLowerCase();
                Predicate phoneMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get(databaseColumn)),
                        "%" + lowerCaseFilter + "%");
                predicates.add(phoneMatch);

            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    private Component createGrid() {
        grid = new Grid<>(Participant.class, false);
        grid.addColumn("telegramId").setAutoWidth(true).setHeader("ИД в tg");
        grid.addColumn("username").setAutoWidth(true).setHeader("Имя пользователя");
        grid.addColumn("alternative").setAutoWidth(true).setHeader("Альтернативное имя");
        grid.addColumn("admin").setAutoWidth(true).setHeader("Роль администратора");
        grid.addColumn("accessible").setAutoWidth(true).setHeader("Есть доступ у бота");

        grid.setItems(query -> participantService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}

package ru.ed.module.bot.update;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/*
изменения статуса бота в чатах:)
 */

@Log4j2
@Component
@HandlerType(type = "myChatMember")
public class MyChatMember implements UpdateHandler {

    @Override
    public void handle(Update update) {

    }

//    private final ChannelService channelService;
//
//    @Autowired
//    public MyChatMember(ChannelService channelService) {
//        this.channelService = channelService;
//    }
//
//    /*
//    добавлен: old ChatMemberLeft -> new ChatMemberMember || ChatMemberAdministrator
//    удален: new ChatMemberLeft
//    подняли до админа: old ChatMemberMember -> new ChatMemberAdministrator
//    сняли с админа: old ChatMemberAdministrator -> new ChatMemberMember
//
//    примеры:
//
//    добавил в чат, из которого был удален:
//
//    ChatMemberUpdated(chat=Chat(id=-772877658, type=group, title=Архком (АТ), firstName=null, lastName=null, userName=null, photo=null, description=null, inviteLink=null, pinnedMessage=null, stickerSetName=null, canSetStickerSet=null, permissions=null, slowModeDelay=null, bio=null, linkedChatId=null, location=null, messageAutoDeleteTime=null, hasPrivateForwards=null, HasProtectedContent=null, joinToSendMessages=null, joinByRequest=null, hasRestrictedVoiceAndVideoMessages=null, isForum=null, activeUsernames=null, emojiStatusCustomEmojiId=null, hasAggressiveAntiSpamEnabled=null, hasHiddenMembers=null, emojiStatusExpirationDate=null, availableReactions=null, accentColorId=null, backgroundCustomEmojiId=null, profileAccentColorId=null, profileBackgroundCustomEmojiId=null, hasVisibleHistory=null, unrestrictBoostCount=null, customEmojiStickerSetName=null), from=User(id=417976333, firstName=Сергей, isBot=false, lastName=Kондитеров, userName=smev_at, languageCode=ru, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null), date=1730106697, oldChatMember=ChatMemberLeft(status=left, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null)), newChatMember=ChatMemberMember(status=member, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null)), inviteLink=null, viaChatFolderInviteLink=null)
//
//    newChatMember = {ChatMemberMember@15786} "ChatMemberMember(status=member, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null))"
//    oldChatMember = {ChatMemberLeft@15785} "ChatMemberLeft(status=left, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null))"
//
//
//    поднял до админа
//
//    ChatMemberUpdated(chat=Chat(id=-772877658, type=group, title=Архком (АТ), firstName=null, lastName=null, userName=null, photo=null, description=null, inviteLink=null, pinnedMessage=null, stickerSetName=null, canSetStickerSet=null, permissions=null, slowModeDelay=null, bio=null, linkedChatId=null, location=null, messageAutoDeleteTime=null, hasPrivateForwards=null, HasProtectedContent=null, joinToSendMessages=null, joinByRequest=null, hasRestrictedVoiceAndVideoMessages=null, isForum=null, activeUsernames=null, emojiStatusCustomEmojiId=null, hasAggressiveAntiSpamEnabled=null, hasHiddenMembers=null, emojiStatusExpirationDate=null, availableReactions=null, accentColorId=null, backgroundCustomEmojiId=null, profileAccentColorId=null, profileBackgroundCustomEmojiId=null, hasVisibleHistory=null, unrestrictBoostCount=null, customEmojiStickerSetName=null), from=User(id=417976333, firstName=Сергей, isBot=false, lastName=Kондитеров, userName=smev_at, languageCode=ru, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null), date=1730106912, oldChatMember=ChatMemberMember(status=member, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null)), newChatMember=ChatMemberAdministrator(status=administrator, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null), canBeEdited=false, customTitle=null, isAnonymous=false, canManageChat=true, canPostMessages=null, canEditMessages=null, canDeleteMessages=true, canRestrictMembers=true, canPromoteMembers=false, canChangeInfo=true, canInviteUsers=true, canPinMessages=true, canManageVideoChats=true, canManageTopics=null, canPostStories=false, canEditStories=false, canDeleteStories=false), inviteLink=null, viaChatFolderInviteLink=null)
//
//    oldChatMember = {ChatMemberMember@15842} "ChatMemberMember(status=member, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null))"
//    newChatMember = {ChatMemberAdministrator@15843} "ChatMemberAdministrator(status=administrator, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null), canBeEdited=false, customTitle=null, isAnonymous=false, canManageChat=true, canPostMessages=null, canEditMessages=null, canDeleteMessages=true, canRestrictMembers=true, canPromoteMembers=false, canChangeInfo=true, canInviteUsers=true, canPinMessages=true, canManageVideoChats=true, canManageTopics=null, canPostStories=false, canEditStories=false, canDeleteStories=false)"
//
//
//    убрал из админов
//
//    myChatMember=ChatMemberUpdated(chat=Chat(id=-772877658, type=group, title=Архком (АТ), firstName=null, lastName=null, userName=null, photo=null, description=null, inviteLink=null, pinnedMessage=null, stickerSetName=null, canSetStickerSet=null, permissions=null, slowModeDelay=null, bio=null, linkedChatId=null, location=null, messageAutoDeleteTime=null, hasPrivateForwards=null, HasProtectedContent=null, joinToSendMessages=null, joinByRequest=null, hasRestrictedVoiceAndVideoMessages=null, isForum=null, activeUsernames=null, emojiStatusCustomEmojiId=null, hasAggressiveAntiSpamEnabled=null, hasHiddenMembers=null, emojiStatusExpirationDate=null, availableReactions=null, accentColorId=null, backgroundCustomEmojiId=null, profileAccentColorId=null, profileBackgroundCustomEmojiId=null, hasVisibleHistory=null, unrestrictBoostCount=null, customEmojiStickerSetName=null), from=User(id=417976333, firstName=Сергей, isBot=false, lastName=Kондитеров, userName=smev_at, languageCode=ru, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null), date=1730106978, oldChatMember=ChatMemberAdministrator(status=administrator, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null), canBeEdited=false, customTitle=null, isAnonymous=false, canManageChat=true, canPostMessages=null, canEditMessages=null, canDeleteMessages=true, canRestrictMembers=true, canPromoteMembers=false, canChangeInfo=true, canInviteUsers=true, canPinMessages=true, canManageVideoChats=true, canManageTopics=null, canPostStories=false, canEditStories=false, canDeleteStories=false), newChatMember=ChatMemberMember(status=member, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null)), inviteLink=null, viaChatFolderInviteLink=null), chatMember=null, chatJoinRequest=null, messageReaction=null, messageReactionCount=null, chatBoost=null, removedChatBoost=null)
//
//    oldChatMember=ChatMemberAdministrator(status=administrator, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null), canBeEdited=false, customTitle=null, isAnonymous=false, canManageChat=true, canPostMessages=null, canEditMessages=null, canDeleteMessages=true, canRestrictMembers=true, canPromoteMembers=false, canChangeInfo=true, canInviteUsers=true, canPinMessages=true, canManageVideoChats=true, canManageTopics=null, canPostStories=false, canEditStories=false, canDeleteStories=false)
//    newChatMember=ChatMemberMember(status=member, user=User(id=7011340125, firstName=my test bot, isBot=true, lastName=null, userName=Object7_bot, languageCode=null, canJoinGroups=null, canReadAllGroupMessages=null, supportInlineQueries=null, isPremium=null, addedToAttachmentMenu=null)), inviteLink=null, viaChatFolderInviteLink=null), chatMember=null, chatJoinRequest=null, messageReaction=null, messageReactionCount=null, chatBoost=null, removedChatBoost=null)
//
//     */
//
//    @Override
//    public void handle(Update update) {
////        добавлен: old ChatMemberLeft -> new ChatMemberMember
////        удален: new ChatMemberLeft
////        подняли до админа: old ChatMemberMember -> new ChatMemberAdministrator
////        сняли с админа: old ChatMemberAdministrator -> new ChatMemberMember
//        ChatMemberUpdated myChatMember = update.getMyChatMember();
//
//        if (myChatMember.getOldChatMember() instanceof ChatMemberLeft && myChatMember.getNewChatMember() instanceof ChatMemberMember) {
//            //бота добавили в чат
//            newChat(myChatMember.getChat());
//        } else if (myChatMember.getNewChatMember() instanceof ChatMemberLeft) {
//            leftChat(myChatMember.getChat());
//            //бот удален из чата
//        } else if (myChatMember.getNewChatMember() instanceof ChatMemberAdministrator) {
//            //бот сделан в админы
//            adminRightsStatus(myChatMember.getChat(), true);
//        } else if (myChatMember.getOldChatMember() instanceof ChatMemberAdministrator && myChatMember.getNewChatMember() instanceof ChatMemberMember) {
//            //бот разжалован из админов
//            adminRightsStatus(myChatMember.getChat(), false);
//        } else {
//            //не обрабатываемый случай
//            log.warn("Не обрабатываемый MyChatMember update: {}", update);
//        }
//    }
//
//    private void newChat(Chat chat) {
//        channelService.add(chat.getTitle(), chat.getId());
//    }
//
//    private void adminRightsStatus(Chat chat, boolean status) {
//        Optional<ChannelEntity> candidate = channelService.find(chat.getId());
//        if (candidate.isPresent()) {
//            ChannelEntity e = candidate.get();
//            ChannelMeta channelMeta = new ChannelMeta(e);
//            channelMeta.setAdminRights(status);
//            channelService.update(e, channelMeta);
//        }
//    }
//
//    private void leftChat(Chat chat) {
//        channelService.delete(chat.getId());
//    }
}

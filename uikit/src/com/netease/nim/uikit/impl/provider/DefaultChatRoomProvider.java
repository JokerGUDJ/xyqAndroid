//package com.netease.nim.uikit.impl.provider;
//
//import com.netease.nim.uikit.api.model.chatroom.ChatRoomProvider;
//
//
///**
// * Created by hzchenkang on 2017/11/2.
// */
//
//public class DefaultChatRoomProvider implements ChatRoomProvider {
//
//    @Override
//    public ChatRoomMember getChatRoomMember(String roomId, String account) {
//        return ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, account);
//    }
//
//    @Override
//    public void fetchMember(String roomId, String account, SimpleCallback<ChatRoomMember> callback) {
//        ChatRoomMemberCache.getInstance().fetchMember(roomId, account, callback);
//    }
//
//    @Override
//    public void fetchRoomMembers(String roomId, MemberQueryType memberQueryType, long time, int limit, SimpleCallback<List<ChatRoomMember>> callback) {
//        ChatRoomMemberCache.getInstance().fetchRoomMembers(roomId, memberQueryType, time, limit, callback);
//    }
//}

package com.pettory.pettory.chat.query.mapper;

import com.pettory.pettory.chat.query.dto.SelectChattingEntityResponse;
import com.pettory.pettory.chat.query.dto.SelectChattingResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChattingMapper {
    List<SelectChattingResponse> selectChatRoomChatting(Integer chatRoomUniqueNum);

    int checkChatroom(Integer chatroomTypeNum);
}

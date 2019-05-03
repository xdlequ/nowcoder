package com.project.dao;

import com.project.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.*;

/**
 * Created by ql on 2019/5/2.
 */
@Mapper
public interface QuestionDao {
    String TABLE_NAME="question";
    String INSERT_FIELD="title, content, comment_count,created_date,user_id";
    String SELECT_FIELD="id, title, content, comment_count,created_date,user_id";

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELD,") values (#{title},#{content},#{comment_count},#{create_date},#{user_id})"})
    int addQuestion(Question question);

    //List<Question>selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);


    List<Question>selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,@Param("limit")int limit);

    @Select({"select ", SELECT_FIELD, " from ", TABLE_NAME, " where id=#{id}"})
    Question selectById(int id);

    @Update({"update",TABLE_NAME,"set commentCount=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id,@Param("commentCount") int commentCount);
}

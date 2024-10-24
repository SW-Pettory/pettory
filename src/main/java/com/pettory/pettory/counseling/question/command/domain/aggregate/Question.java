package com.pettory.pettory.counseling.question.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "counseling_question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE counseling_question SET counseling_question_state = 'DELETE', counseling_question_delete_datetime = NOW() WHERE counseling_question_num = ?")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counseling_question_num")
    private Integer counselingQuestionNum;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "counseling_question_title")
    private String counselingQuestionTitle;
    @Column(name = "counseling_question_content")
    private String counselingQuestionContent;
    @Column(name = "counseling_question_hits")
    private Integer counselingQuestionHits;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "counseling_question_state")
    private QuestionStatus counselingQuestionState  = QuestionStatus.WAIT;
    @CreatedDate
    @Column(name = "counseling_question_insert_datetime")
    private LocalDateTime counselingQuestionInsertDatetime;
    @Column(name = "counseling_question_delete_datetime")
    private LocalDateTime counselingQuestionDeleteDatetime;
    @LastModifiedDate
    @Column(name = "counseling_question_update_datetime")
    private LocalDateTime counselingQuestionUpdateDatetime;
    @Column(name = "counseling_question_file_url")
    private String counselingQuestionFileUrl;

    private Question(Integer userId, String counselingQuestionTitle, String counselingQuestionContent, Integer counselingQuestionHits, String counselingQuestionFileUrl) {
        this.userId = userId;
        this.counselingQuestionTitle = counselingQuestionTitle;
        this.counselingQuestionContent = counselingQuestionContent;
        this.counselingQuestionHits = counselingQuestionHits;
        this.counselingQuestionFileUrl = counselingQuestionFileUrl;
    }

    public static Question create(Integer userId, String counselingQuestionTitle, String counselingQuestionContent, Integer counselingQuestionHits, String counselingQuestionFileUrl) {
        return new Question(userId, counselingQuestionTitle, counselingQuestionContent, counselingQuestionHits, counselingQuestionFileUrl);
    }

    public void changeCounselingQuestionFileUrl(String counselingQuestionFileUrl) {
        this.counselingQuestionFileUrl = counselingQuestionFileUrl;
    }

    public void updateQuestionDetails(Integer userId, String counselingQuestionTitle, String counselingQuestionContent, Integer counselingQuestionHits, QuestionStatus counselingQuestionState) {
        this.userId = userId;
        this.counselingQuestionTitle = counselingQuestionTitle;
        this.counselingQuestionContent = counselingQuestionContent;
        this.counselingQuestionHits = counselingQuestionHits;
        this.counselingQuestionState = counselingQuestionState;
    }
}

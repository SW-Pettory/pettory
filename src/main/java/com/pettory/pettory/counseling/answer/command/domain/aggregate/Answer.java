package com.pettory.pettory.counseling.answer.command.domain.aggregate;

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
@Table(name = "counseling_answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE counseling_answer SET counseling_answer_state = 'DELETE', counseling_answer_delete_datetime = NOW() WHERE counseling_answer_num = ?")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counseling_answer_num")
    private Integer counselingAnswerNum;
    @Column(name = "counseling_question_num")
    private Integer counselingQuestionNum;
    @Column(name = "counseling_answer_content")
    private String counselingAnswerContent;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "counseling_answer_state")
    private AnswerStatus counselingAnswerState  = AnswerStatus.ACTIVE;
    @CreatedDate
    @Column(name = "counseling_answer_insert_datetime")
    private LocalDateTime counselingAnswerInsertDatetime;
    @Column(name = "counseling_answer_delete_datetime")
    private LocalDateTime counselingAnswerDeleteDatetime;
    @LastModifiedDate
    @Column(name = "counseling_answer_update_datetime")
    private LocalDateTime counselingAnswerUpdateDatetime;
    @Column(name = "counseling_answer_reanswer_num")
    private Integer counselingAnswerReanswerNum;
    @Column(name = "counseling_answer_file_url")
    private String counselingAnswerFileUrl;

    private Answer(Integer counselingQuestionNum, String counselingAnswerContent, Integer counselingAnswerReanswerNum, String counselingAnswerFileUrl) {
        this.counselingQuestionNum = counselingQuestionNum;
        this.counselingAnswerContent = counselingAnswerContent;
        this.counselingAnswerReanswerNum = counselingAnswerReanswerNum;
        this.counselingAnswerFileUrl = counselingAnswerFileUrl;
    }

    public static Answer create(Integer counselingQuestionNum, String counselingAnswerContent, Integer counselingAnswerReanswerNum, String counselingAnswerFileUrl) {
        return new Answer(counselingQuestionNum, counselingAnswerContent, counselingAnswerReanswerNum, counselingAnswerFileUrl);
    }

    public void changeCounselingAnswerFileUrl(String counselingAnswerFileUrl) {
        this.counselingAnswerFileUrl = counselingAnswerFileUrl;
    }

    public void updateAnswerDetails(Integer counselingQuestionNum, String counselingAnswerContent, AnswerStatus counselingAnswerState, Integer counselingAnswerReanswerNum) {
        this.counselingQuestionNum = counselingQuestionNum;
        this.counselingAnswerContent = counselingAnswerContent;
        this.counselingAnswerState = counselingAnswerState;
        this.counselingAnswerReanswerNum = counselingAnswerReanswerNum;
    }
}

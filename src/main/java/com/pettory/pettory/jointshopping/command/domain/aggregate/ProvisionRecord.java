package com.pettory.pettory.jointshopping.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "provision_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)  // 엔티티 생성, 삭제 시점 체크를 위해 필요한 리스너
@SQLDelete(sql = "UPDATE provision_record " +
        "SET provision_state = 'Completion', provision_complete_datetime = NOW() " +
        "WHERE provision_record_num = ?")
public class ProvisionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long provisionRecordNum;
    private Integer provisionCost;
    @Enumerated(value = EnumType.STRING)
    private ProvisionState provisionState = ProvisionState.Waiting;
    @CreatedDate
    private LocalDateTime provisionRecordInsertDatetime;
    private LocalDateTime provisionCompleteDatetime;

    @ManyToOne
    @JoinColumn(name = "joint_shopping_group_num", nullable = false)
    JointShoppingGroup jointShoppingGroup;

    private ProvisionRecord(Integer provisionCost, JointShoppingGroup jointShoppingGroup){
        this.provisionCost = provisionCost;
        this.jointShoppingGroup = jointShoppingGroup;
    }

    public static ProvisionRecord create(Integer provisionCost, JointShoppingGroup jointShoppingGroup) {
        return new ProvisionRecord(provisionCost, jointShoppingGroup);
    }

    /* 지급완료 상태로 변경하는 메소드 */
    public void changeProvisionState() {
        this.provisionState = ProvisionState.Completion;
        this.provisionCompleteDatetime = LocalDateTime.now();
    }
}

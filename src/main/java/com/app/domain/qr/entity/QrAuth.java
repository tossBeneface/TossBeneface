package com.app.domain.qr.entity;

import com.app.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

@Entity
@Getter
@Setter
public class QrAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nonce;

    @Column(nullable = false)
    private boolean authenticated;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}

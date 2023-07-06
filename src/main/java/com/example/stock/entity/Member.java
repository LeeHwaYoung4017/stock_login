package com.example.stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data //Lombok 라이브러리 Getter,Setter,toString,equals,hashCode 등 메서드를 생성
@Entity // JPA(Java Persistence API)에서 엔티티 클래스를 지정하는데 사용
@Table(name = "member") // 엔티티 클래스와 매핑되는 데이터베이스 테이블 이름을 지정
@NoArgsConstructor // 매개변수가 없는 기본생성자를 생성
@AllArgsConstructor // 모든 필드를 매개변수로 갖는 생성자를 생성
public class Member {

    @Id //엔티티 클래스의 기본 키를 지정하는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue 기본 키의 자동생성 전략을 지정 / GenerationType.IDENTITY 데이터베이스에서 자동으로 기본 키 값을 생성
    private Long id;

    @Column(name = "username")
    private String username;
    private String password;
    private String name;
    private String email;
    private String address1;
    private String address2;
    private String address3;
    private String managerCheck;
    private Date registrationDate;


}

package com.example.demo.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)    //  ignore all null fields
@Entity(name = "users")
@Table(name = "users")
@NoArgsConstructor
public class Users {
    @Id
    @SequenceGenerator(name = "users_sequence", allocationSize = 1, sequenceName = "users_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    @Column(updatable = false)
    @Getter(onMethod = @__(@JsonIgnore)) // generate the getter with the specific annotation.
    @Setter
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    @Getter
    @Setter
    private String name;

    @NotNull
    @Column(name = "pwd", nullable = false, length = 50)
    @Getter
    @Setter
    private String pwd;

    @JsonCreator
    public Users(@JsonProperty("name") String name, @JsonProperty("pwd") String pwd) {
        this.name = name;
        this.pwd = pwd;
    }
}

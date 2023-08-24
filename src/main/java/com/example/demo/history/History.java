package com.example.demo.history;

import com.example.demo.users.Users;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)    //  ignore all null fields
@Entity(name = "history")
@Table(name = "history")
@NoArgsConstructor
public class History {
    @Id
    @SequenceGenerator(name = "privilege_sequence", allocationSize = 1, sequenceName = "privilege_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privilege_sequence")
    @Column(updatable = false)
    @Getter(onMethod = @__(@JsonIgnore)) // generate the getter with the specific annotation.
    @Setter
    private Integer id;


    @Column(name = "logtime")
    @Getter
    @Setter
    private LocalDateTime logTime;


    @NotNull
    @Column(name = "result", nullable = false)
    @Getter
    @Setter
    private Boolean result;

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users;

    @JsonCreator
    public History(@JsonProperty("timeConnection") LocalDateTime logTime, @JsonProperty("result") Boolean result, Users users) {
        this.logTime = logTime;
        this.result = result;
        this.users = users;
    }
}

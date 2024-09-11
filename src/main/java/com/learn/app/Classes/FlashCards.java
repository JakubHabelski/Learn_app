package com.learn.app.Classes;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "FlashCards")
public class FlashCards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long FlashCardId;
    private Long SetID;
    private String Definition;
    private String Description;

    private String path;
    @Column(columnDefinition = "boolean default false") // Ustawienie domyślnej wartości na false
    private boolean Learned;


    @ManyToOne
    @JoinColumn(name = "SetID", insertable = false, updatable = false)
    private FlashCardSet flashCardSet;

    @OneToOne(mappedBy = "flashCard", cascade = CascadeType.ALL)
    private image image;
    @Column(columnDefinition = "int default 0")
    private Integer rep_Num; // Repetition number
    private Date next_rep; // Next repetition

    @Column(columnDefinition = "int default 0")
    private Integer time_out; // Interval between repetitions
    @Column(columnDefinition = "float default 2.5")
    private float EF; // Easiness factor
    @Column(columnDefinition = "tinyint default 0")
    private byte last_user_grade;

    private String def_lang;
    private String des_lang;

    public FlashCards(Long flashCardId, Long setID, String definition, String description, boolean learned) {
        FlashCardId = flashCardId;
        SetID = setID;
        Definition = definition;
        Description = description;
        Learned = learned;


    }


    public FlashCards() {
    }

    public Long getFlashCardId() {
        return FlashCardId;
    }

    public void setFlashCardId(Long flashCardId) {
        FlashCardId = flashCardId;
    }

    public Long getSetID() {
        return SetID;
    }

    public void setSetID(Long setID) {
        SetID = setID;
    }

    public String getDefinition() {
        return Definition;
    }

    public void setDefinition(String definition) {
        Definition = definition;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getLearned() {
        return Learned;
    }

    public void setLearned(boolean learned) {
        Learned = learned;
    }

    public Integer getRep_Num() {
        return rep_Num;
    }

    public void setRep_Num(Integer rep_Num) {
        this.rep_Num = rep_Num;
    }

    public Integer getTime_out() {
        return time_out;
    }

    public void setTime_out(Integer time_out) {
        this.time_out = time_out;
    }

    public float getEF() {
        return EF;
    }

    public void setEF(float EF) {
        this.EF = EF;
    }

    public Date getNext_rep() {
        return next_rep;
    }

    public void setNext_rep(LocalDate next_rep) {
        this.next_rep = java.sql.Date.valueOf(next_rep);
    }

    public byte getLast_user_grade() {
        return last_user_grade;
    }

    public void setLast_user_grade(byte q) {
        this.last_user_grade = q;
    }

    public String getDef_lang() {
        return def_lang;
    }

    public void setDef_lang(String def_lang) {
        this.def_lang = def_lang;
    }

    public String getDes_lang() {
        return des_lang;
    }

    public void setDes_lang(String des_lang) {
        this.des_lang = des_lang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashCards that = (FlashCards) o;
        return Objects.equals(Definition, that.Definition) &&
                Objects.equals(Description, that.Description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Definition, Description);
    }
}






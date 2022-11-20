package org.hbrs.se2.project.entities;

import org.hbrs.se2.project.wrapper.StudentHasMajorKey;

import javax.persistence.*;

@Entity @IdClass(StudentHasMajorKey.class)
@Table(name = "student_has_major", schema = "mid9db")
public class StudentHasMajor {
    private int studentid;
    private int majorid;

    @Id
    @Column(name = "studentid")
    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    @Id
    @Column(name = "majorid")
    public int getMajorid() {
        return majorid;
    }

    public void setMajorid(int majorid) {
        this.majorid = majorid;
    }

}

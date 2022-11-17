package org.hbrs.se2.project.wrapper;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public class StudentHasMajorKey implements Serializable{
    private int studentid;
    private int majorid;

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public int getMajorid() {
        return majorid;
    }

    public void setMajorid(int majorid) {
        this.majorid = majorid;
    }



}

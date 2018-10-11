/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author tobia
 */
public class Module {
    private String mod_id;
    private String mod_name;
    private String mod_desc;

    public Module(String id, String name, String desc) {
        this.mod_id = id;
        this.mod_name = name;
        this.mod_desc = desc;
    }
    
    public Module() {
        
    }
    
    public void createDeliverables() {
        /*
        SELECT student_id FROM student
        INNER JOIN studentlist ON student.student_id = studentlist.student_id
        INNER JOIN course ON studenlist.course_id = course.course_id;
        INNER JOIN module ON course.course_id = module.course_id;
        */
    }
    
}

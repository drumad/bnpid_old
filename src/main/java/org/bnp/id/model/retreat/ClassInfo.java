package org.bnp.id.model.retreat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bnp.id.constants.ClassType;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassInfo {

    private ClassType classType;

    private Date beginDate;

    private Date endDate;

    private Integer id;

    private String classNo;
}
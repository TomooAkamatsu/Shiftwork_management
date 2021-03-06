package com.example.sma.infrastructure.employee;

import com.example.sma.domain.models.employee.Employee;
import com.example.sma.domain.models.employee.Security;
import com.example.sma.domain.models.employee.WorkingForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface EmployeeRepository {

    //    従業員の全件取得
    public List<Employee> findActiveEmployee();

    //    従業員を新規登録
    public int insertEmployee(Employee employee);

    //    従業員情報を更新
    public void updateEmployee(Employee employee);

    //    従業員を削除
    public void deleteEmployee(int employeeId);

    //    勤務形態の全件取得
    public List<WorkingForm> findAllWorkingForm();

    //    従業員の一件検索
    Optional<Employee> findOneEmployee(int employeeId);

    void deleteAllEmployee();

    void insertAllEmployee();

    void alterTableEmployee();

    Optional<Security> getLoginInfo(int employeeId);

    void patchRetirement(@Param("employeeId") int employeeId,@Param("date") String date);

    List<Employee> findActiveEmployeeInTheMonth(@Param("year") int year,@Param("month") int month);
}

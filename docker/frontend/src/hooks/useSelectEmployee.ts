import { useCallback, useState } from "react";
import { typeEmployee } from "../type/typeEmployee";

type Props = {
  employeeId: number;
  employeesData: Array<typeEmployee>;
  onOpen: () => void;
};

export const useSelectEmployee = () => {
  const [selectedEmployee, setSelectedEmployee] = useState<typeEmployee | null>(
    null
  );

  const onSelectEmployee = useCallback((props: Props) => {
    const { employeeId, onOpen, employeesData } = props;
    const targetEmployee = employeesData.find(
      (employee) => employee.employeeId === employeeId
    );
    setSelectedEmployee(targetEmployee ?? null);
    onOpen();
  }, []);

  return { onSelectEmployee, selectedEmployee };
};

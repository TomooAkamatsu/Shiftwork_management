import { useCallback, useState } from "react";
import { instance } from "../api/axios";
import { typeVacationRequestList } from "../type/typeVacationRequestList";

export const useVacationRequestList = () => {
  const [vacationRequestList, setVacationRequestList] = useState<
    Array<typeVacationRequestList>
  >([]);
  const [loading, setLoading] = useState(false);

  const getVacationRequestList = useCallback(() => {
    setLoading(true);
    instance
      .get<Array<typeVacationRequestList>>("shift/vacation-requests")
      .then((res) => {
        setVacationRequestList(res.data);
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);
  return { vacationRequestList, getVacationRequestList, loading };
};

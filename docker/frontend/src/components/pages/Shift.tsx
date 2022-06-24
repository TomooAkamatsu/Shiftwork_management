import { Box, Center, HStack, Spinner } from "@chakra-ui/react";
import { memo, useCallback, useEffect, useState, VFC } from "react";
import { useHistory } from "react-router-dom";
import { PrimaryButton } from "../atoms/button/PrimaryButton";
import { Table, Thead, Tbody, Tr, Th, Td } from "@chakra-ui/react";
import { useShiftList } from "../../hooks/useShiftList";

export const Shift: VFC = memo(() => {
  const history = useHistory();
  const [year, setYear] = useState(new Date().getFullYear());
  const [month, setMonth] = useState(new Date().getMonth());

  const { shiftData, getShift, loading } = useShiftList(year, month + 1);

  const onClickVacationRequest = useCallback(
    () => history.push("/shiftwork_management/shift/request"),
    [history]
  );
  const onClickShiftCreation = useCallback(
    () => history.push("/shiftwork_management/shift/new"),
    [history]
  );

  const today = new Date();
  const monthOfNowPage = new Date(year, month, 1);

  const lastMonthOfNowPage = new Date(year, month - 1, 1);
  const onClickLastMonth = () => {
    setYear(lastMonthOfNowPage.getFullYear());
    setMonth(lastMonthOfNowPage.getMonth());
  };

  const nextMonthOfNowPage = new Date(year, month + 1, 1);
  const onClickNextMonth = () => {
    setYear(nextMonthOfNowPage.getFullYear());
    setMonth(nextMonthOfNowPage.getMonth());
  };

  const beginningOfTheMonth = new Date(
    monthOfNowPage.getFullYear(),
    monthOfNowPage.getMonth(),
    1
  );
  const endOfTheMonth = new Date(
    monthOfNowPage.getFullYear(),
    monthOfNowPage.getMonth() + 1,
    0
  );

  const dateList = [];

  useEffect(() => {
    getShift();
  }, [getShift]);

  for (
    var d = beginningOfTheMonth;
    d <= endOfTheMonth;
    d.setDate(d.getDate() + 1)
  ) {
    dateList.push(new Date(d));
  }

  // 一時的な処理です。根本的にどうするか考えなければ。
  const employeeNameArr = [
    "岸田",
    "菅",
    "安倍",
    "野田",
    "菅",
    "鳩山",
    "麻生",
    "福田",
    "小泉",
    "森",
  ];

  return (
    <>
      {loading ? (
        <Center h="40vh">
          <Spinner size="xl" />
        </Center>
      ) : (
        <Box textAlign="center" p={5}>
          <Box>
            <HStack justify="center">
              <Box>
                <PrimaryButton
                  onClick={onClickLastMonth}
                >{`${lastMonthOfNowPage.getFullYear()}年
      ${lastMonthOfNowPage.getMonth() + 1}月`}</PrimaryButton>
              </Box>
              <Box>
                <h1 style={{ fontSize: "20px", fontWeight: "bold" }}>
                  {`${monthOfNowPage.getFullYear()}年${
                    monthOfNowPage.getMonth() + 1
                  }月のシフト`}
                </h1>
              </Box>
              <Box>
                {today.getFullYear() === monthOfNowPage.getFullYear() &&
                today.getMonth() === monthOfNowPage.getMonth() ? (
                  <Box w={142}></Box>
                ) : (
                  <PrimaryButton
                    onClick={onClickNextMonth}
                  >{`${nextMonthOfNowPage.getFullYear()}年
      ${nextMonthOfNowPage.getMonth() + 1}月`}</PrimaryButton>
                )}
              </Box>
            </HStack>
          </Box>
          <Table size="sm" variant="striped" colorScheme="blackAlpha">
            <Thead>
              <Tr>
                <Th p={1} textAlign="center">
                  名前
                </Th>
                {dateList.map((date, index) => (
                  <Th key={index} p={1} textAlign="center">{`${
                    date.getMonth() + 1
                  }/${date.getDate()}`}</Th>
                ))}
              </Tr>
            </Thead>
            <Tbody>
              {shiftData.map((shift, index) => (
                <Tr key={index}>
                  <Td textAlign="center" p={1}>
                    {employeeNameArr[index]}
                  </Td>
                  {shift.patternArr.map((shiftPattern, index) => (
                    <Td textAlign="center" p={1} key={index}>
                      {shiftPattern}
                    </Td>
                  ))}
                </Tr>
              ))}
            </Tbody>
          </Table>
          <PrimaryButton onClick={onClickVacationRequest}>
            休み希望の提出確認
          </PrimaryButton>
          <PrimaryButton onClick={onClickShiftCreation}>
            来月のシフト作成
          </PrimaryButton>
        </Box>
      )}
    </>
  );
});

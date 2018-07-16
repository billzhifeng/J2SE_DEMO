package com.utils;

public class TddlDemo {

    public static void main(String[] args) {
        System.out.println(getTbAndDbnameNew("fin_core_pay", "bgw_order", "12345678", 8, 128));
    }

    /**
     * @param tableName
     * @param dbName
     * @param divValue
     * @param dbCount 库总数
     * @param tableCount 每个库里面的 tableCount
     * @return
     */
    public static String getTbAndDbnameNew(String tableName, String dbName, String divValue, Integer dbCount,
                                           Integer tableCount) {
        int idivValue = Math.abs(divValue.hashCode());

        Integer tableValue = idivValue % (dbCount * tableCount);
        String tableValueStr = leftPad(tableValue, 4, '0');//表[0000,9999] 4位数表示

        Integer dbValue = tableValue / tableCount;
        String dbValueStr = leftPad(dbValue, 2, '0');//库[00,99]

        StringBuffer sb = new StringBuffer();
        sb.append(dbName + "_" + dbValueStr);
        sb.append("." + tableName + "_" + tableValueStr + " ");
        return sb.toString();
    }

    private static String leftPad(int num, final int maxLen, char filledChar) {
        StringBuffer sb = new StringBuffer();
        String str = String.valueOf(num);
        for (int i = str.length(); i < maxLen; i++) {
            sb.append(filledChar);
        }
        return sb.append(str).toString();
    }

}

/**
 * InfectStatistic
 * TODO
 *
 * @author WeiNan Zhao
 * @version 1.0.0
 * @since 2020.01.15
 */


enum PatientType {

    INFECTION("��Ⱦ����","ip"),
    SUSPECTED("���ƻ���","sp"),
    CURE("����","cure"),
    DEAD("����", "dead");

    private String typeName;
    private String abbr;

    PatientType(String typename, String abbr) {
        this.typeName = typename;
        this.abbr = abbr;
    }
    public String getTypeName() { return typeName; }
    public String getAbbr() { return abbr; }

    public static PatientType getTypeByAbbr(String abbr) {
        for(PatientType patientType : PatientType.values()) {
            if(abbr.equals(patientType.getAbbr())) {
                return patientType;
            }
        }
        return null;
    }

}


class ProvinceComparator implements Comparator<String> {

    private static final String PROVINCE_STRING = "ȫ�� ���� ���� ���� ���� "
            + "���� �㶫 ���� ���� ���� �ӱ� ���� ������ ���� ���� ���� ���� ���� "
            + "���� ���ɹ� ���� �ຣ ɽ�� ɽ�� ���� �Ϻ� �Ĵ� ��� ���� �½� ���� �㽭";

    @Override
    public int compare(String s1, String s2) {
        return PROVINCE_STRING.indexOf(s1) - PROVINCE_STRING.indexOf(s2);
    }

    public static boolean isExist(String provinceName) {
        return PROVINCE_STRING.contains(provinceName);
    }

}


class Province {

    private static int[] nationalNumbers;
    private int[] localNumbers;

    Province() { localNumbers = new int[PatientType.values().length]; }

    public static int[] getNationalNumbers() { return nationalNumbers; }

    public int[] getLocalNumbers() { return localNumbers; }

    public static void setNationalNumbers(int[] nationalNumbers) {
        Province.nationalNumbers = nationalNumbers;
    }

    public void alterLocalNum(PatientType patientType, int changedNum) {
        localNumbers[patientType.ordinal()] += changedNum;
        nationalNumbers[patientType.ordinal()] += changedNum;
    }

}


class InfectStatistic {
    public static void main(String[] args) {
        
    }
}

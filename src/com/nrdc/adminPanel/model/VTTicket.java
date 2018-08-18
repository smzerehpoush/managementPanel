package com.nrdc.adminPanel.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "VT_TICKET", schema = "MOBILE")
public class VTTicket {
    @Id
    @Column(name = "ID_VT_TICKET")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "RANGE_ID")
    private String rangeId;

    @Column(name = "PLATE")
    private String plate;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LICENSE_NUMBER")
    private String licenseNumber;

    @Column(name = "ENF_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    //java.util.Date
    private Date enfDate;

    @Column(name = "IS_BIG")
    private String isBig;

    @Column(name = "IS_INTERNAL")
    private String isInternal;

    @Column(name = "REGISTER_CITY_ID")
    private String registerCityId;

    @Column(name = "LICENSE_CITY_ID")
    private String licenseCityId;

    @Column(name = "LICENSE_TYPE_ID")
    private String licenseTypeId;

    @Column(name = "LICENSE_ISSUE_CITY")
    private String licenseIssueCity;

    @Column(name = "CAR_COLOR_ID")
    private String carColorId;

    @Column(name = "POLICE_CODE")
    private String policeCode;

    @Column(name = "TICKET_TYPE_ID")
    private String ticketTypeId;

    @Column(name = "CAR_SYSTEM_ID")
    private String carSystemId;

    @Column(name = "CAR_USAGE_ID")
    private String carUsageId;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ZONE")
    private String zone;

    @Column(name = "ORGANIZATION")
    private String organization;

    @Column(name = "LICENSE_DATE")
    private String licenseDate;

    @Column(name = "FINE_PRICE")
    private String finePrice;

    @Column(name = "NATIONAL_ID")
    private String nationalId;

    @Column(name = "PAPER_ID")
    private String paperId;

    @Column(name = "PAYMENT_ID")
    private String paymentId;

    @Column(name = "IS_PREVENT")
    private String isPrevent;

    @Column(name = "WARNING_POLICE_DESC")
    private String warningPoliceDesc;

    @Column(name = "VIOLATION_ID_1")
    private String violationId1;

    @Column(name = "PEOPLE_NO_1")
    private String peopleNo1;

    @Column(name = "VIOLATION_ID_2")
    private String violationId2;

    @Column(name = "PEOPLE_NO_2")
    private String peopleNo2;

    @Column(name = "VIOLATION_ID_3")
    private String violationId3;

    @Column(name = "PEOPLE_NO_3")
    private String peopleNo3;

    @Column(name = "POLICE_STATION_CODE")
    private String policeStationCode;

    @Column(name = "SESSION_ID")
    private String sessionId;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "FK_USER_ID")
    private String fkUserId;

    @Column(name = "LATITUDE")
    private String latitude;

    @Column(name = "LONGITUDE")
    private String longitude;


    public VTTicket() {
    }

    public Date getEnfDate() {
        return enfDate;
    }

    public void setEnfDate(Date enfDate) {
        this.enfDate = enfDate;
    }

    @Override
    public String toString() {
        return "VTTicket{" +
                "id=" + id +
                ", rangeId='" + rangeId + '\'' +
                ", plate='" + plate + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", enfDate=" + enfDate +
                ", address='" + address + '\'' +
                ", finePrice='" + finePrice + '\'' +
                ", paperId='" + paperId + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", violationId1='" + violationId1 + '\'' +
                ", peopleNo1='" + peopleNo1 + '\'' +
                ", violationId2='" + violationId2 + '\'' +
                ", violationId3='" + violationId3 + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", status='" + status + '\'' +
                ", fkUserId='" + fkUserId + '\'' +
                '}';
    }

    /**
     * @param rangeId it must be just 10 digit
     * @return returns true if value is just 10 digit
     * @throws Exception return exception when range id is not 10 digit
     */
    private boolean checkRangeId(String rangeId) throws Exception {
        if (rangeId != null && !rangeId.isEmpty() && rangeId.length() == 10)
            return true;
        else
            throw new Exception("رنج جریمه معتبر نیست.");
    }

    /**
     * @param plate it must be just 20 digit
     * @return returns true if value is just 20 digit
     * @throws Exception return exception when value is not 20 digit
     */
    private boolean checkPlate(String plate) throws Exception {
        if (plate != null && !plate.isEmpty() && plate.length() <= 20)
            return true;
        else
            throw new Exception("کد پلاک معتبر نمی باشد.");
    }


    private boolean checkTicketTypeId(Integer ticketTypeId) throws Exception {
        List<Integer> trueValues = new ArrayList<>();
        trueValues.add(1);
        trueValues.add(2);
        trueValues.add(3);
        trueValues.add(4);
        trueValues.add(5);

        if (ticketTypeId != null) {
            for (Integer trueValue : trueValues)
                if (ticketTypeId.equals(trueValue))
                    return true;
            throw new Exception("نوع جریمه معتبر نیست.");
        } else {
            throw new Exception("نوع جریمه دارای مقدار نیست.");
        }
    }

    private boolean check01(String key, String value) throws Exception {
        if (value != null && !value.isEmpty()) {
            if (value.equals("0") || value.equals("1"))
                return true;
            else
                throw new Exception("value of " + key + " is not valid");
        } else {
            throw new Exception("value of " + key + " is not valid");
        }
    }

    public String getFkUserId() {
        return fkUserId;
    }

    private void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    private void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    private void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRangeId() {
        return rangeId;
    }

    private void setRangeId(String rangeId) {
        this.rangeId = rangeId;
    }

    public String getPlate() {
        return plate;
    }

    private void setPlate(String plate) {
        this.plate = plate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    private void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getIsBig() {
        return isBig;
    }

    private void setIsBig(String isBig) {
        this.isBig = isBig;
    }

    public String getIsInternal() {
        return isInternal;
    }

    private void setIsInternal(String isInternal) {
        this.isInternal = isInternal;
    }

    public String getRegisterCityId() {
        return registerCityId;
    }

    private void setRegisterCityId(String registerCityId) {
        this.registerCityId = registerCityId;
    }

    public String getLicenseCityId() {
        return licenseCityId;
    }

    private void setLicenseCityId(String licenseCityId) {
        this.licenseCityId = licenseCityId;
    }

    public String getCarColorId() {
        return carColorId;
    }

    private void setCarColorId(String carColorId) {
        this.carColorId = carColorId;
    }

    public String getPoliceCode() {
        return policeCode;
    }

    public void setPoliceCode(String policeCode) {
        this.policeCode = policeCode;
    }

    public String getTicketTypeId() {
        return ticketTypeId;
    }

    private void setTicketTypeId(String ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public String getCarSystemId() {
        return carSystemId;
    }

    private void setCarSystemId(String carSystemId) {
        this.carSystemId = carSystemId;
    }

    public String getCarUsageId() {
        return carUsageId;
    }

    private void setCarUsageId(String carUsageId) {
        this.carUsageId = carUsageId;
    }

    public String getAddress() {
        return address;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    public String getZone() {
        return zone;
    }

    private void setZone(String zone) {
        this.zone = zone;
    }

    public String getOrganization() {
        return organization;
    }

    private void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getLicenseDate() {
        return licenseDate;
    }

    private void setLicenseDate(String licenseDate) {
        this.licenseDate = licenseDate;
    }

    public String getLicenseTypeId() {
        return licenseTypeId;
    }

    private void setLicenseTypeId(String licenseTypeId) {
        this.licenseTypeId = licenseTypeId;
    }

    public String getLicenseIssueCity() {
        return licenseIssueCity;
    }

    private void setLicenseIssueCity(String licenseIssueCity) {
        this.licenseIssueCity = licenseIssueCity;
    }

    public String getFinePrice() {
        return finePrice;
    }

    private void setFinePrice(String finePrice) {
        this.finePrice = finePrice;
    }

    public String getNationalId() {
        return nationalId;
    }

    private void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPaperId() {
        return paperId;
    }

    private void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    private void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getIsPrevent() {
        return isPrevent;
    }

    private void setIsPrevent(String isPrevent) {
        this.isPrevent = isPrevent;
    }

    public String getWarningPoliceDesc() {
        return warningPoliceDesc;
    }

    private void setWarningPoliceDesc(String warningPoliceDesc) {
        this.warningPoliceDesc = warningPoliceDesc;
    }

    public String getViolationId1() {
        return violationId1;
    }

    private void setViolationId1(String violationId1) {
        this.violationId1 = violationId1;
    }

    public String getPeopleNo1() {
        return peopleNo1;
    }

    private void setPeopleNo1(String peopleNo1) {
        this.peopleNo1 = peopleNo1;
    }

    public String getViolationId2() {
        return violationId2;
    }

    private void setViolationId2(String violationId2) {
        this.violationId2 = violationId2;
    }

    public String getPeopleNo2() {
        return peopleNo2;
    }

    private void setPeopleNo2(String peopleNo2) {
        this.peopleNo2 = peopleNo2;
    }

    public String getViolationId3() {
        return violationId3;
    }

    private void setViolationId3(String violationId3) {
        this.violationId3 = violationId3;
    }

    public String getPeopleNo3() {
        return peopleNo3;
    }

    private void setPeopleNo3(String peopleNo3) {
        this.peopleNo3 = peopleNo3;
    }

    public String getSessionId() {
        return sessionId;
    }

    private void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoliceStationCode() {
        return policeStationCode;
    }

    private void setPoliceStationCode(String policeStationCode) {
        this.policeStationCode = policeStationCode;
    }
}

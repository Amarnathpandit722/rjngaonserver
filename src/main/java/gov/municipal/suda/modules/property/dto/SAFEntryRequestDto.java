package gov.municipal.suda.modules.property.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.municipal.suda.modules.property.model.transaction.SAFARVDetailsBean;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SAFEntryRequestDto {
    @NotNull(message="prop_id shouldn't be null")
    //below number provided by SUDA manually
    private String prop_id;  // table to insert tbl_prop_usr_rel_rec, tbl_prop_transaction_dtls, tbl_prop_owner_details (prop_id), manual entered by user, aka property no
// Adding Floor Details in Zone Id
    //    @NotNull(message="zone_id shouldn't be null")
//    private Long zone_id;  // get from tbl_zone_mstr and insert
    @NotNull(message="ward_id shouldn't be null")
    private Long ward_id; // get from tbl_ward_mstr (ward_name), insert into tbl_property_mstr
    @NotNull(message="Financial year id shouldn't be null")
    private Long fy_id; // get from tbl_fy table
    @NotNull(message="entry_type_id shouldn't be null")
    private Long entry_type_id; // New Assessment/Re Assessment/Mutation/Demand Entry/Assessment/Legacy Entry/Legacy Data from tbl_property_entry_type tableprivate String entry_type; // insert into tbl_property_mstr (entry_type field)
    @NotNull(message="entry_type_name shouldn't be null")
    private String entry_type_name; //New Assessment/Re Assessment/Mutation/Demand Entry/Assessment/Legacy Entry/Legacy Data from tbl_property_entry_type tableprivate String entry_type; // insert into tbl_property_mstr (entry_type field)
    @NotNull(message="property_type_id shouldn't be null")
    private Long property_type_id; // get id from tbl_property_type
    @NotNull(message="road_type_id shouldn't be null")
    private Long road_type_id; // get id from tbl_road_type
    @NotNull(message="area_id shouldn't be null")
    private Long area_id; // get id from tbl_area_zone_mstr
    @NotNull(message="module_id shouldn't be null")
    private Long module_id; // get id from tbl_module_mstr
    @NotNull(message="module_name shouldn't be null")
    private String module_name; // get module_name from tbl_module_mstr
    @NotNull(message="user_id shouldn't be null")
    private Long user_id; // get user_id from users table
    @NotNull(message="prop_owner_name shouldn't be null")
    private String prop_owner_name; // insert into tbl_prop_owner_details(owner_name)
    @NotNull(message="owner_title shouldn't be null")
    private String owner_title; // get data from title drop-down (Mr, Mrs etc)
    @NotNull(message="owner_gender shouldn't be null")
    private String owner_gender; // get from drop-down Gender
    @NotNull(message="father_name shouldn't be null")
    private String father_name; // insert into tbl_prop_owner_details(guardian_name)
    @NotNull(message="prop_address shouldn't be null")
    private String prop_address; // insert into tbl_prop_owner_details(owner_address)
    @NotNull(message="purchase_date shouldn't be null")
    private String purchase_date; // get from purchase date box (DD-MM-YYYY)
    @NotNull(message="owner_relation shouldn't be null")
    private String owner_relation; // get from relation drop-down (S/O, D/O etc)
    private String city;
    @NotNull(message="Pin code shouldn't be null")
    private String pin;
    @NotNull(message="district shouldn't be null")
    private String district;
    private String mohalla;
    private String vsrno;
    private BigDecimal plot_area; // calculation for vaccant land
    private String plot_no;
    private String khata_no;
    @NotNull(message="mobile_no shouldn't be blank") // It is mandatory as confirmation from Mr Chandan
    private Long mobile_no;  // insert into tbl_prop_owner_details(mobile_no)
    private Long aadhar; //@NotNull(message="Pan No shouldn't be null")
    private String panno;
    private String email;
    private String rain_harvest; // Flag for rain harvest (yes or no) (present under the tbl_property_mstr)
    private String rain_water_docs; // get this from uploaded rain water documents fields, if Flag is Yes
    private String isMobileTower; // Yes or No input value
    private String owner_pic;

    //@NotNull(message="uses_type_id shouldn't be null")
    //private Long uses_type_id;  // (residential/commercial/industrial) insert into tbl_property_mstr(usage_type_id) get this id from tbl_uses_type
    @NotNull(message="total_built_up_area shouldn't be null")
    private BigDecimal total_built_up_area;   // Calculate the property tax on this value and insert into tbl_property_mstr

    @NotNull(message="effective_date shouldn't be null")
    private String effective_date; // get it from tbl_ratechange_effect table (effect_date), check for Entry type re-assessment like
    //in which year his/her last payment record is present in tbl_prop_last_payment_record table, fetch that effective date + 1 (ex: if
    //last payment record present in the year of 2019-2020 than effective_date will be the next financial year date like 2020-2021 till date or
    // up to what ever citizen want to give the payment. compare that financial year and get the exact effective date from tbl_ratechange_effect table.

   // private String occupancy_type; // get from tbl_occupation_type (occup_type) and insert into tbl_prop_arv_detail (prop_id, zone_id, occupancy_type, construction_type, floor_name) value= self/rental

    private Long old_ward_id; // insert into tbl_property_mstr (old_ward_id) incase of Legacy entry
    private Integer prop_age_count; // if age count is > 20  set Yes  otherwise No of Old property field
    private String is_widow;
    private String is_handicapped;
    private String is_isdp;
    private String is_school;
    private String is_complex;
    @NotNull(message ="floor_details Year shouldn't be null" )
    private List<SAFARVDetailsDTO> floor_details;
    @NotNull(message="Financial Year shouldn't be null")
    private String financial_year; // get if from  tbl_fy (fy_name field) dynamically
    @NotNull(message="Current Financial Year shouldn't be null")
    private String current_financial_year; // Ex: 2022-2023 find recent financial year from tbl_fy (fy_name field)
    private String construction_date_from; // DD/MM/YYYY take it from page Building Construction Period From => for calculate the age of the building, and also insert into tbl_saf_arv_detail (completion_date)
    private String construction_date_upto; // DD/MM/YYYY take it from page Building Construction Period To => for calculate the age of the building, and also insert into tbl_saf_arv_detail (compupto_date)
    private String isMobileRequest;
    //private List<MultipartFile> imagesUpload;
    //private PropertyGeoLocationDTO propertyGeoLocation;
    /*
    private BigDecimal prop_tax_resi; // insert into tbl_prop_owner_tax_mstr (prop_id,property_tax), and tbl_prop_collection_mstr (prop_id, property_tax)
    private BigDecimal prop_tax_commer; // insert into tbl_prop_collection_mstr (prop_id,Aproperty_tax)
    private BigDecimal prop_tax_indust; // insert into tbl_prop_collection_mstr (prop_id,Aproperty_tax)
    private BigDecimal open_land_area; // measure the rate from tbl_vacantland_rate table.
    private BigDecimal annual_rental_resi;
    private BigDecimal annual_rental_comm;
    private BigDecimal annual_rental_indust;
    private BigDecimal total_annual_rental;
    private BigDecimal total_annual_rental_disc;
    private BigDecimal net_payable_prop_tax_perc;
    private BigDecimal payable_prop_tax_fifty_per_disc;
    private BigDecimal discount_under_sec_136;
    private BigDecimal special_disc;
    private BigDecimal edu_cess;
    private BigDecimal consolidate_tax;
    private BigDecimal arrears_of_prop_tax;
    private BigDecimal arrears_of_consolidate_tax;
    private BigDecimal late_fee;
    private BigDecimal shasti_sulk_chrg;
    private BigDecimal rain_wtr_harv_chrg; // measure the rate from tbl_rain_harvest_mstr on total buildup area value
    private BigDecimal saf_form_chrg;
    private BigDecimal tot_pay_amnt;
    private BigDecimal tot_rec_amnt;
    private BigDecimal tot_adv_amnt;   // newly added  // insert into tbl_prop_advance (ward_id, prop_id, amount) if any
    private BigDecimal tot_pend_amnt;  //newly added
*/
    private String govTap_Conn;
    private String buildingType; // Check value govermetn or private;
    private String buildingAge;  // Check the Age of Bulding ;
    
    private String b1;
    private String b2;
    private String diversion;
    private String kharidi_Bikri;
    
    
    
    
    
    
}

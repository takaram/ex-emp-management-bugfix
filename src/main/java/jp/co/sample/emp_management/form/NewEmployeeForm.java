package jp.co.sample.emp_management.form;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

public class NewEmployeeForm {
	/** 従業員名 */
	@NotBlank(message = "従業員名を入力してください")
	private String name;
	/** 画像 */
	private MultipartFile image;
	/** 性別 */
	@NotNull(message = "性別を選択してください")
	private String gender;
	/** 入社日 */
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@NotNull(message = "入社日を入力してください")
	private Date hireDate;
	/** メールアドレス */
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "メールアドレスの書式が不正です")
	private String mailAddress;
	/** 郵便番号の上3桁 */
	@Pattern(regexp = "\\d{3}", message = "正しい郵便番号を入力してください")
	private String zipCode1;
	/** 郵便番号の下4桁 */
	@Pattern(regexp = "\\d{4}", message = "正しい郵便番号を入力してください")
	private String zipCode2;
	/** 住所 */
	@NotBlank(message = "住所を入力してください")
	private String address;
	/** 電話番号 */
	@NotBlank(message = "電話番号を入力してください")
	private String telephone;
	/** 給料 */
	@NotNull(message = "給料を入力してください")
	private Integer salary;
	/** 特性 */
	@NotBlank(message = "特性を入力してください")
	private String characteristics;
	/** 扶養人数 */
	@NotNull(message = "扶養人数を入力してください")
	private Integer dependentsCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getzipCode() {
		return zipCode1 + "-" + zipCode2;
	}

	public void setzipCode(String zipCode) {
		String[] zipCodeParts = zipCode.split("-");
		zipCode1 = zipCodeParts[0];
		zipCode2 = zipCodeParts[1];
	}

	public String getzipCode1() {
		return zipCode1;
	}

	public void setzipCode1(String zipCode1) {
		this.zipCode1 = zipCode1;
	}

	public String getzipCode2() {
		return zipCode2;
	}

	public void setzipCode2(String zipCode2) {
		this.zipCode2 = zipCode2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public String getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}

	public Integer getDependentsCount() {
		return dependentsCount;
	}

	public void setDependentsCount(Integer dependentsCount) {
		this.dependentsCount = dependentsCount;
	}
}

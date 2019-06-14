package jp.co.sample.emp_management.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.sample.emp_management.domain.Employee;

/**
 * employeesテーブルを操作するリポジトリ.
 * 
 * @author igamasayuki
 * 
 */
@Repository
public class EmployeeRepository {

	/**
	 * Employeeオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER = (rs, i) -> {
		Employee employee = new Employee();
		employee.setId(rs.getInt("id"));
		employee.setName(rs.getString("name"));
		employee.setImage(rs.getString("image"));
		employee.setGender(rs.getString("gender"));
		employee.setHireDate(rs.getDate("hire_date"));
		employee.setMailAddress(rs.getString("mail_address"));
		employee.setZipCode(rs.getString("zip_code"));
		employee.setAddress(rs.getString("address"));
		employee.setTelephone(rs.getString("telephone"));
		employee.setSalary(rs.getInt("salary"));
		employee.setCharacteristics(rs.getString("characteristics"));
		employee.setDependentsCount(rs.getInt("dependents_count"));
		return employee;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * 従業員一覧情報を入社日順で取得します.
	 * 
	 * @return 全従業員一覧 従業員が存在しない場合はサイズ0件の従業員一覧を返します
	 */
	public List<Employee> findAll() {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count "
				+ "FROM employees ORDER BY hire_date DESC";

		List<Employee> developmentList = template.query(sql, EMPLOYEE_ROW_MAPPER);

		return developmentList;
	}

	/**
	 * 指定したlimit、offsetで従業員情報を取得します.
	 * 取得順は入社日の降順(重複の場合はIDの降順)です。
	 *
	 * @param limit 取得件数
	 * @param offset オフセット
	 * @return 従業員情報のリスト
	 */
	public List<Employee> findAll(int limit, int offset) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count "
				+ "FROM employees ORDER BY hire_date DESC, id DESC LIMIT :limit OFFSET :offset";
		SqlParameterSource params = new MapSqlParameterSource().addValue("limit", limit).addValue("offset", offset);
		return template.query(sql, params, EMPLOYEE_ROW_MAPPER);
	}

	/**
	 * 登録されている従業員の件数を取得します.
	 * 
	 * @return データベースに登録されている件数
	 */
	public int getSize() {
		String sql = "SELECT COUNT(*) FROM employees;";
		return template.queryForObject(sql, (SqlParameterSource) null, Integer.class);
	}

	/**
	 * 主キーから従業員情報を取得します.
	 * 
	 * @param id 検索したい従業員ID
	 * @return 検索された従業員情報
	 * @exception 従業員が存在しない場合は例外を発生します
	 */
	public Employee load(Integer id) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees WHERE id=:id";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		Employee development = template.queryForObject(sql, param, EMPLOYEE_ROW_MAPPER);

		return development;
	}

	/**
	 * 従業員情報を変更します.
	 */
	public void update(Employee employee) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(employee);

		String updateSql = "UPDATE employees SET dependents_count=:dependentsCount WHERE id=:id";
		template.update(updateSql, param);
	}

	/**
	 * 名前で従業員情報を曖昧検索します.
	 *
	 * @param name 名前
	 * @return 見つかった従業員情報のリスト. 0人の場合は空のリストになります。
	 */
	public List<Employee> searchByName(String name) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count "
				+ "FROM employees WHERE name LIKE :name ORDER BY hire_date DESC;";
		SqlParameterSource params = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		return template.query(sql, params, EMPLOYEE_ROW_MAPPER);
	}

	/**
	 * 名前で従業員情報を曖昧検索します.
	 *
	 * @param name 名前
	 * @param limit 取得件数
	 * @param offset オフセット
	 * @return 見つかった従業員情報のリスト. 0人の場合は空のリストになります。
	 */
	public List<Employee> searchByName(String name, int limit, int offset) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count "
				+ "FROM employees WHERE name LIKE :name ORDER BY hire_date DESC, id DESC LIMIT :limit OFFSET :offset;";
		SqlParameterSource params = new MapSqlParameterSource()
				.addValue("name", "%" + name + "%").addValue("limit", limit).addValue("offset", offset);
		return template.query(sql, params, EMPLOYEE_ROW_MAPPER);
	}

	/**
	 * 名前で検索してヒットする件数を取得します.
	 * 
	 * @param name 名前
	 * @return ヒット件数
	 */
	public int getSize(String name) {
		String sql = "SELECT COUNT(*) FROM employees WHERE name LIKE :name;";
		SqlParameterSource  param = new MapSqlParameterSource("name", "%" + name + "%");
		return template.queryForObject(sql, param, Integer.class);
	}
}

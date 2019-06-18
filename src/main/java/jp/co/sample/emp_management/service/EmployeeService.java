package jp.co.sample.emp_management.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.repository.EmployeeRepository;

/**
 * 従業員情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class EmployeeService {
	private static final int EMPLOYEES_PER_PAGE = 10;

	@Autowired
	private EmployeeRepository employeeRepository;
	
	/**
	 * 従業員情報を全件取得します.
	 * 
	 * @return　従業員情報一覧
	 */
	public List<Employee> showList() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}
	
	/**
	 * 従業員情報を10件ずつ取得します.
	 * 
	 * @param page ページ番号 (1オリジン)
	 * @return 取得した従業員情報のリスト
	 */
	public List<Employee> showList(int page) {
		if (page < 1) {
			throw new IllegalArgumentException("ページ番号は1以上でなければなりません");
		}
		int offset = (page - 1) * EMPLOYEES_PER_PAGE;
		return employeeRepository.findAll(EMPLOYEES_PER_PAGE, offset);
	}

	/**
	 * 従業員リストの最大のページ数を取得します.
	 * 
	 * @return 従業員リストの最大のページ数
	 */
	public int getMaxPageCount() {
		int size = employeeRepository.getSize();
		return (size - 1) / EMPLOYEES_PER_PAGE + 1;
	}

	/**
	 * 従業員情報を取得します.
	 * 
	 * @param id ID
	 * @return 従業員情報
	 * @throws 検索されない場合は例外が発生します
	 */
	public Employee showDetail(Integer id) {
		Employee employee = employeeRepository.load(id);
		return employee;
	}
	
	/**
	 * 従業員情報を更新します.
	 * 
	 * @param employee　更新した従業員情報
	 */
	public void update(Employee employee) {
		employeeRepository.update(employee);
	}

	/**
	 * 名前の部分一致で従業員情報を検索します.
	 *
	 * @param name 検索する名前
	 * @return 見つかった従業員情報のリスト.
	 */
	public List<Employee> search(String name) {
		return employeeRepository.searchByName(name);
	}

	/**
	 * 名前の部分一致で従業員情報を検索します.
	 *
	 * @param name 検索する名前
	 * @param page ページ番号(1オリジン)
	 * @return 見つかった従業員情報のリスト.
	 */
	public List<Employee> search(String name, int page) {
		if (page < 1) {
			throw new IllegalArgumentException("ページ番号は1以上でなければなりません");
		}
		int offset = (page - 1) * EMPLOYEES_PER_PAGE;
		return employeeRepository.searchByName(name, EMPLOYEES_PER_PAGE, offset);
	}

	/**
	 * 名前検索での従業員リストの最大のページ数を取得します.
	 * 
	 * @param name 検索する名前
	 * @return 名前検索での従業員リストの最大のページ数
	 */
	public int getMaxPageCount(String name) {
		int size = employeeRepository.getSize(name);
		System.out.println(size);
		return (size - 1) / EMPLOYEES_PER_PAGE + 1;
	}

	/**
	 * 従業員情報をデータベースに登録します.
	 *
	 * @param employee 登録するEmployeeオブジェクト
	 */
	public void create(Employee employee) {
		employeeRepository.insert(employee);
	}

	/**
	 * アップロードされた画像を保存します.
	 *
	 * @param uploadedFile 保存するファイル
	 * @return 保存先のファイル名
	 * @throws IOException 保存に失敗した場合
	 */
	public String saveFile(MultipartFile uploadedFile) throws IOException {
		String origFilename = uploadedFile.getOriginalFilename();
		String extension = origFilename.substring(origFilename.lastIndexOf(".") + 1);
		File dest = createNewFile(extension);
		uploadedFile.transferTo(dest);

		return dest.getName();
	}

	/**
	 * 画像保存用の新しいファイルを作成します.
	 *
	 * @param extension ファイルの拡張子
	 * @return 作成したファイルを表すFileオブジェクト
	 * @throws IOException ファイルの作成に失敗した場合
	 */
	private File createNewFile(String extension) throws IOException {
		File imagesRoot = ResourceUtils.getFile("classpath:public/img");
		File file = new File(imagesRoot, UUID.randomUUID() + "." + extension);
		file.createNewFile();
		return file;
	}
}

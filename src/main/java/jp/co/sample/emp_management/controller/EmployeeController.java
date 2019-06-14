package jp.co.sample.emp_management.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.form.UpdateEmployeeForm;
import jp.co.sample.emp_management.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * 
	 * @param page ページ番号
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/showList")
	public String showList(Integer page, Model model) {
		if (page == null) {
			page = 1;
		}
		List<Employee> employeeList = employeeService.showList(page);
		model.addAttribute("employeeList", employeeList);

		model.addAttribute("currentPage", page);
		int maxPage = employeeService.getMaxPageCount();
		model.addAttribute("maxPage", maxPage);
		model.addAttribute("shownPageRange", calcPageRange(page, maxPage));

		String url = "/employee/showList";
		model.addAttribute("url", url);

		List<Employee> allEmployeeList = employeeService.showList();
		model.addAttribute("allEmployeeList", allEmployeeList);

		return "employee/list";
	}

	/**
	 * 入力した名前に部分一致する従業員の一覧画面を出力します.
	 *
	 * @param name 検索する名前
	 * @param page ページ番号
	 * @param model モデル
	 * @return 検索でヒットした従業員の一覧画面
	 */
	@RequestMapping("/search")
	public String search(String name, Integer page, Model model) {
		if (page == null) {
			page = 1;
		}

		List<Employee> employeeList = employeeService.search(name, page);
		int maxPage = employeeService.getMaxPageCount(name);
		if (employeeList.isEmpty()) {
			model.addAttribute("message", "名前に「" + name + "」を含む従業員は見つかりませんでした");
			employeeList = employeeService.showList(page);
			maxPage = employeeService.getMaxPageCount();
		}
		model.addAttribute("employeeList", employeeList);

		model.addAttribute("currentPage", page);
		model.addAttribute("maxPage", maxPage);
		model.addAttribute("shownPageRange", calcPageRange(page, maxPage));

		try {
			String url = "/employee/search?name=" + URLEncoder.encode(name, "UTF-8");
			model.addAttribute("url", url);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}

		List<Employee> allEmployeeList = employeeService.showList();
		model.addAttribute("allEmployeeList", allEmployeeList);

		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}
	
	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form
	 *            従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@RequestMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}

	/**
	 * ページ番号を表示する範囲を決定します.
	 * 現在のページが1に近い場合は1～10、最大ページに近い場合は最後の10ページ、
	 * それ以外の場合は現在のページ-5から10ページです。
	 * 
	 * @param currentPage 現在のページ数
	 * @param maxPage 最後のページ数
	 * @return 表示する最初のページと最後のページが入った配列
	 */
	private int[] calcPageRange(int currentPage, int maxPage) {
		int minShownPage = Math.max(1, Math.min(currentPage - 5, maxPage - 9));
		int maxShownPage = Math.min(Math.max(10, currentPage + 4), maxPage);
		return new int[] {minShownPage, maxShownPage};
	}
}

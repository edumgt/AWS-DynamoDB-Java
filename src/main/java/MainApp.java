import java.util.Scanner;
import com.cognizant.Employee;
import com.cognizant.EmployeeManager;

public class MainApp {
    public static void main(String[] args) {
        EmployeeManager manager = new EmployeeManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== 직원관리 예시 (AWS DynamoDB) ===");
            System.out.println("1. 직원 추가");
            System.out.println("2. 직원 조회");
            System.out.println("3. 직원 삭제");
            System.out.println("4. 모든 직원 보기");
            System.out.println("5. 종료");
            System.out.print("1~5 중 선택하세요: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1 -> {
                        System.out.print("ID: ");
                        String id = sc.nextLine();

                        System.out.print("Name: ");
                        String name = sc.nextLine();

                        System.out.print("Position: ");
                        String position = sc.nextLine();

                        Employee emp = new Employee(id, name, position);
                        manager.addEmployee(emp);
                    }
                    case 2 -> {
                        System.out.print("조회할 ID 입력: ");
                        String viewId = sc.nextLine();

                        System.out.print("조회할 이름 입력: ");
                        String viewName = sc.nextLine();

                        manager.viewEmployee(viewId, viewName); // ✅ name 추가
                    }
                    case 3 -> {
                        System.out.print("삭제할 ID 입력: ");
                        String delId = sc.nextLine();

                        System.out.print("삭제할 이름 입력: ");
                        String delName = sc.nextLine();

                        manager.deleteEmployee(delId, delName); // ✅ name 추가
                    }
                    case 4 -> manager.listEmployees();
                    case 5 -> {
                        System.out.println("👋 프로그램을 종료합니다.");
                        sc.close();
                        return;
                    }
                    default -> System.out.println("❗ 1~5 사이의 숫자를 입력하세요.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ 숫자만 입력해주세요.");
            } catch (Exception e) {
                System.out.println("🚨 예외 발생: " + e.getMessage());
            }
        }
    }
}

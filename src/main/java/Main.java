import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static final String separator = "######################";
    public static final String sepLeft = "<<<<<<<<";
    public static final String sepRight = ">>>>>>>>";
    public static boolean error;
    public static boolean returnMenu;
    public static int selectMenu;
    public static List<News> newsList = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        CRUD crud = new CRUD("https://palichmmm.ru",
                new HashMap<>(){{
                    put(CRUD.READ_ALL, "api/news/index");
                    put(CRUD.READ_PAGE, "api/news/index?page=");
                    put(CRUD.READ_ONE, "api/news/view?id=");
                    put(CRUD.CREATE, "api/news/create");
                    put(CRUD.UPDATE, "api/news/update?id=");
                    put(CRUD.DELETE, "api/news/delete?id=");
                }}
                );

        while (true) {
            menu(" Меню ", new String[] {"Новости", "Выход из программы"});
            switch (inputInt()) {
                case 1: {
                    while (true) {
                        returnMenu = false;
                        menu(" Новости ", new String[] {"Показать все","Показать одну","Создать","Изменить","Удалить","Назад"});
                        switch (inputInt()) {
                            case 1: {
                                error = parseNews(crud.readAll().getJson());
                                if (!error) {
                                    System.out.println("\n" + sepLeft + " Список новостей " + sepRight);
                                    for (News news : newsList) {
                                        System.out.println(news);
                                    }
                                    crud.pagination();
                                    int page;
                                    // Выбор страницы
                                    while (true) {
                                        System.out.print("Введите номер страницы (0 - Меню): ");
                                        page = inputInt();
                                        if (page == 0) {
                                            break;
                                        }
                                        if (error) {
                                            continue;
                                        } else {
                                            error = parseNews(crud.readPage(page).getJson());
                                            if (!error) {
                                                System.out.println("\n" + sepLeft + " Список новостей " + sepRight);
                                                for (News news : newsList) {
                                                    System.out.println(news);
                                                }
                                                crud.pagination();
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                            case 2: {
                                int newsId;
                                while (true) {
                                    System.out.print("\nВведите номер новости: ");
                                    newsId = inputInt();
                                    if (error) {
                                        continue;
                                    }
                                    if (newsId <= 0) {
                                        System.err.println("Число должно быть больше нуля!\n");
                                    } else {
                                        break;
                                    }
                                }
                                error = parseNews(crud.readOne(newsId).getJson());
                                if (!error) {
                                    showNews(newsList);
                                }
                                break;
                            }
                            case 3: {
                                News tempNews = new News();
                                System.out.println("\n" + sepLeft + " Создать новость " + sepRight);
                                while (true) {
                                    System.out.print("\nВВЕДИТЕ ТИП НОВОСТИ :");
                                    tempNews.setTypeId(inputInt());
                                    if (!error) {
                                        break;
                                    }
                                }
                                System.out.print("ВВЕДИТЕ ЗАГОЛОВОК: ");
                                tempNews.setNameNews(scanner.nextLine());
                                System.out.print("ВВЕДИТЕ КРАТКОЕ ОПИСАНИЕ: ");
                                tempNews.setShortDesc(scanner.nextLine());
                                System.out.print("ВВЕДИТЕ ПОЛНОЕ ОПИСАНИЕ: ");
                                tempNews.setFullDesc(scanner.nextLine());
                                System.out.print("ВВЕДИТЕ ИСТОЧНИК: ");
                                tempNews.setSource(scanner.nextLine());
                                error = parseNews(crud.create(tempNews).getJson());
                                if (!error) {
                                    showNews(newsList);
                                }
                                break;
                            }
                            case 4: {
                                System.out.println("\n" + sepLeft + " Изменить новость " + sepRight);
                                int newsId;
                                String input;
                                while (true) {
                                    System.out.print("\nВведите номер новости: ");
                                    newsId = inputInt();
                                    if (error) {
                                        continue;
                                    }
                                    if (newsId <= 0) {
                                        System.err.println("Число должно быть больше нуля!\n");
                                    } else {
                                        break;
                                    }
                                }
                                error = parseNews(crud.readOne(newsId).getJson());
                                if (!error) {
                                    System.out.println("\n" + sepRight + "ИЗМЕНЕНИЕ НОВОСТИ №" + newsList.get(0).getId() +
                                            " Дата: " + newsList.get(0).getDateCreate());
                                    while (true) {
                                        System.out.println("\n" + sepRight + "ТИП НОВОСТИ: " + newsList.get(0).getTypeId());
                                        System.out.print(sepRight + "ВВЕДИТЕ НОВЫЙ ТИП НОВОСТИ (Пропустить - <Enter>): ");
                                        input = scanner.nextLine();
                                        if (!input.equals("")) {
                                            try {
                                                newsList.get(0).setTypeId(Integer.parseInt(input));
                                            } catch (Exception err) {
                                                System.err.println("Вы ввели не число!\n");
                                                continue;
                                            }
                                            break;
                                        } else {
                                            break;
                                        }
                                    }
                                    System.out.println("\n" + sepRight + "ЗАГОЛОВОК: " + newsList.get(0).getNameNews());
                                    System.out.print(sepRight + "ВВЕДИТЕ НОВЫЙ ЗАГОЛОВОК (Пропустить - <Enter>): ");
                                    input = scanner.nextLine();
                                    if (!input.equals("")) {
                                        newsList.get(0).setNameNews(input);
                                    }
                                    System.out.println("\n" +sepRight + "КРАТКОЕ ОПИСАНИЕ: " + newsList.get(0).getShortDesc());
                                    System.out.print(sepRight + "ВВЕДИТЕ КРАТКОЕ ОПИСАНИЕ (Пропустить - <Enter>): ");
                                    input = scanner.nextLine();
                                    if (!input.equals("")) {
                                        newsList.get(0).setShortDesc(input);
                                    }
                                    System.out.println("\n" +sepRight + "ПОЛНОЕ ОПИСАНИЕ: " + newsList.get(0).getFullDesc());
                                    System.out.print(sepRight + "ВВЕДИТЕ ПОЛНОЕ ОПИСАНИЕ (Пропустить - <Enter>): ");
                                    input = scanner.nextLine();
                                    if (!input.equals("")) {
                                        newsList.get(0).setFullDesc(input);
                                    }
                                    System.out.println("\n" +sepRight + "ИСТОЧНИК: " + newsList.get(0).getSource());
                                    System.out.print(sepRight + "ВВЕДИТЕ ИСТОЧНИК (Пропустить - <Enter>): ");
                                    input = scanner.nextLine();
                                    if (!input.equals("")) {
                                        newsList.get(0).setSource(input);
                                    }
                                    error = parseNews(crud.update(newsList.get(0)).getJson());
                                    if (!error) {
                                        System.out.println("\n" + sepLeft + " Результат изменения " + sepRight);
                                        showNews(newsList);
                                    }
                                }
                                break;
                            }
                            case 5: {
                                int newsId;
                                while (true) {
                                    System.out.println("\n!!!!!!!!!!!! ВНИМАНИЕ УДАЛЕНИЕ БЕЗВОЗВРАТНО !!!!!!!!!!!!");
                                    System.out.print("Введите номер новости для её удаления: ");
                                    newsId = inputInt();
                                    if (error) {
                                        continue;
                                    }
                                    if (newsId <= 0) {
                                        System.err.println("Число должно быть больше нуля!\n");
                                    } else {
                                        break;
                                    }
                                }
                                error = parseNews(crud.delete(newsId).getJson());
                                if (!error) {
                                    System.err.println("Новость №" + newsId + " успешно удалена!!!\n");
                                } else {
                                    System.err.println("Не удалось удалить новость №" + newsId + "\n");
                                }
                                break;
                            }
                            case 6: {
                                returnMenu = true;
                                break;
                            }
                        }
                        if (returnMenu) {
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    System.out.println("Вы вышли из программы!");
                    System.exit(1);
                }
                default: {
                    if (!error) {
                        System.err.println("Нет такого номера меню!\n");
                    }
                }
            }
        }
    }
    public static void menu(String nameMenu, String[] menu) {
        System.out.println("\n" + separator + nameMenu + separator);
        for (int i = 0; i < menu.length; i++) {
            System.out.println((i + 1) + ". " + menu[i]);
        }
        System.out.print("Введите номер меню: ");
    }
    public static int inputInt() {
        try {
            String input = scanner.nextLine();
            selectMenu = Integer.parseInt(input);
            error = false;
        } catch (Exception err) {
            System.err.println("Вы ввели не число!\n");
            error = true;
        }
        return selectMenu;
    }
    public static boolean parseNews(String json) {
        newsList.clear();
        if (!json.startsWith("[") && !json.endsWith("]")) {
            json = "[".concat(json).concat("]");
        }
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(json);
            JSONArray jsonArray = (JSONArray) object;
            for (Object obj : jsonArray) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                News news = gson.fromJson(String.valueOf(obj), News.class);
                newsList.add(news);
            }
            if (newsList.get(0) == null) throw new IllegalArgumentException();
        } catch (Exception err) {
            System.err.println("Что то пошло не так! " + json + "\n");
            return true;
        }
        return false;
    }
    public static void showNews(List<News> list) {
        for (News news : list) {
            System.out.println(
                    "\n" + sepRight + "НОВОСТЬ №" + news.getId() +
                    " Дата: " + news.getDateCreate() + " тип: " + news.getTypeId() +
                    "\n" + sepRight + "ЗАГОЛОВОК: " + news.getNameNews() +
                    "\n" + sepRight + "КРАТКОЕ ОПИСАНИЕ: " + news.getShortDesc() +
                    "\n" + sepRight + "ПОЛНОЕ ОПИСАНИЕ: " + news.getFullDesc() +
                    "\n" + sepRight + "ИСТОЧНИК: " + news.getSource()
            );
        }
    }
}

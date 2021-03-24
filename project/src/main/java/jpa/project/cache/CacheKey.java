package jpa.project.cache;

public class CacheKey {
    public static final int DEFAULT_EXPIRE_SEC = 60; // 1 minutes
    public static final String MEMBER = "member";
    public static final int MEMBER_EXPIRE_SEC = 60 * 5; // 5 minutes
    public static final String BOARD = "board";
    public static final int BOARD_EXPIRE_SEC = 60 * 10; // 10 minutes
    public static final String BOARDS = "boards";
    public static final String TOKEN = "token";
    public static final String COMMENTS = "comments";
    public static final String ORDERS_PURCHASE = "orders_purchase";
    public static final String ORDERS_SALES = "orders_sales";
    public static final String ORDERS = "orders";
    public static final int ORDER_EXPIRE_SEC = 60 * 5; // 5 minutes
    public static final String ORDER = "order";
    public static final String REGISTEDSHOES = "RegistedShoes";
    public static final String REGISTEDSHOES_LIST = "RegistedShoes_List";
    public static final int REGISTEDSHOES_EXPIRE_SEC = 60 * 5; // 5 minutes
}
package service;

import dao.CartDAO;
import model.Cart;

import java.util.List;

public class CartService {

    private final CartDAO cartDAO = new CartDAO();

    // Thêm vào giỏ hàng nếu chưa tồn tại
    public boolean addToCart(int learnerID, int courseID) {
        return cartDAO.addToCart(learnerID, courseID);
    }

    // Lấy số lượng mục trong giỏ hàng (trạng thái pending)
    public int getCartItemCount(int learnerID) {
        return cartDAO.getCartItemCount(learnerID);
    }

    // Lấy danh sách các item đang pending trong giỏ hàng
    public List<Cart> getPendingCartItems(int learnerID) {
        return cartDAO.getPendingCartItems(learnerID);
    }

    // Xóa 1 item khỏi giỏ hàng
    public boolean removeFromCart(int cartID, int learnerID) {
        return cartDAO.removeFromCart(cartID, learnerID);
    }

    // (Có thể bổ sung sau) Cập nhật trạng thái giỏ hàng sau khi thanh toán
    public boolean updateStatusAfterPurchase(Cart cart) {
        return cartDAO.updateStatusAfterPurchased(cart);
    }
}

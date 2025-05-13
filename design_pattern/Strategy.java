package design_pattern;

// 策略接口
interface DiscountStrategy {
    double calculateDiscount(double price);
}

// 具体策略A：普通折扣
class NormalDiscount implements DiscountStrategy {
    @Override
    public double calculateDiscount(double price) {
        return price * 0.1; // 10%折扣
    }
}

// 具体策略B：VIP折扣
class VipDiscount implements DiscountStrategy {
    @Override
    public double calculateDiscount(double price) {
        return price * 0.2; // 20%折扣
    }
}

// 具体策略C：新用户折扣
class NewUserDiscount implements DiscountStrategy {
    @Override
    public double calculateDiscount(double price) {
        return price * 0.15; // 15%折扣
    }
}

// 上下文类
class ShoppingCart {
    private DiscountStrategy discountStrategy;

    // 设置折扣策略
    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    // 计算最终价格
    public double calculateFinalPrice(double price) {
        return price - discountStrategy.calculateDiscount(price);
    }
}


public class Strategy {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        double price = 100.0;
        // 使用普通折扣策略
        cart.setDiscountStrategy(new NormalDiscount());
        double finalPrice1 = cart.calculateFinalPrice(price);
        System.out.println("Final Price with Normal Discount: " + finalPrice1);
        // 使用VIP折扣策略
        cart.setDiscountStrategy(new VipDiscount());
        double finalPrice2 = cart.calculateFinalPrice(price);
        System.out.println("Final Price with VIP Discount: " + finalPrice2);
        // 使用新用户折扣策略
        cart.setDiscountStrategy(new NewUserDiscount());
        double finalPrice3 = cart.calculateFinalPrice(price);
        System.out.println("Final Price with New User Discount: " + finalPrice3);
    }
}

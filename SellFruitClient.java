package testsocket.bst;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 水果抽象父类
abstract class Fruit {
	protected String name;
	protected double price;
	protected double weight;
	protected ISingleDiscountStrategy singleDiscountStrategy;
	
	public Fruit(String name, double price, double weight, ISingleDiscountStrategy singleDiscountStrategy) {
		super();
		this.name = name;
		this.price = price;
		this.weight = weight;
		this.singleDiscountStrategy = singleDiscountStrategy;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public ISingleDiscountStrategy getSingleDiscountStrategy() {
		return singleDiscountStrategy;
	}
	public void setSingleDiscountStrategy(ISingleDiscountStrategy singleDiscountStrategy) {
		this.singleDiscountStrategy = singleDiscountStrategy;
	}

	@Override
	public String toString() {
		return "Fruit [name=" + name + ", price=" + price + ", weight=" + weight + "]";
	}
	
	protected double getSinglePrice() {
		if ( null!=this.singleDiscountStrategy ) {
			return this.singleDiscountStrategy.singleDiscountStrategy(this);
		}
		return this.getPrice() * this.getWeight();
	}
}

// 单品打折策略
interface ISingleDiscountStrategy {
	double singleDiscountStrategy(Fruit fruit);
}

// 总价打折策略
interface ITotalDiscountStrategy {
	double singleDiscountStrategy(double totalPrice);
}

// 具体水果类：苹果
class Apple extends Fruit {
	public Apple(String name, double price, double weight, ISingleDiscountStrategy singleDiscountStrategy) {
		super(name, price, weight, singleDiscountStrategy);
	}
}

//具体水果类：草莓
class Strawberry extends Fruit {
	public Strawberry(String name, double price, double weight, ISingleDiscountStrategy singleDiscountStrategy) {
		super(name, price, weight, singleDiscountStrategy);
	}
}

// 价格计算工具类
class CountTotalPriceUtils {
	public static double countTotalPrice(List<Fruit> fruitList, ITotalDiscountStrategy totalDiscountStrategy) {
		System.out.println("购买的水果详情：");
		double totalPrice = 0;
		for ( Fruit fruit : fruitList ) {
			if ( null==fruit ) {
				continue;
			}
			System.out.println(fruit);
			totalPrice = totalPrice + fruit.getSinglePrice();
		}
		
		if ( null!=totalDiscountStrategy ) {
			  totalPrice = totalDiscountStrategy.singleDiscountStrategy(totalPrice);
		}
		return totalPrice;
	}
}

// 此处是时间线，上面是旧版本代码，后面是扩展的代码和客户端代码 ================================

// 扩展一种水果：芒果
class Mango extends Fruit {
	public Mango(String name, double price, double weight, ISingleDiscountStrategy singleDiscountStrategy) {
		super(name, price, weight, singleDiscountStrategy);
	}
}

// 客户端
public class SellFruitClient {
	public static void main(String[] args) {
		// 顾客A
		customerA();
		
		// 顾客B
		customerB();
		
		// 顾客C
		customerC();
		
		// 顾客D
		customerD();
	}
	
	// 计算顾客A的购买费用
	private static void customerA() {
		List<Fruit> fruitList = new ArrayList<>();
		
		Fruit apple = new Apple("apple", 8, 3, null);
		fruitList.add(apple);
		
		Fruit strawberry = new Strawberry("strawberry", 13, 2, null);
		fruitList.add(strawberry);
		
		double totalPrice = CountTotalPriceUtils.countTotalPrice(fruitList, null);
		System.out.println("顾客A购买总价：" + totalPrice);
		System.out.println("=======================================");
	}
	
	// 计算顾客B的购买费用
	private static void customerB() {
		List<Fruit> fruitList = new ArrayList<>();
		
		Fruit apple = new Apple("apple", 8, 3, null);
		fruitList.add(apple);
		
		Fruit strawberry = new Strawberry("strawberry", 13, 2, null);
		fruitList.add(strawberry);
		
		Fruit mango = new Mango("mango", 20, 1, null);
		fruitList.add(mango);
		
		double totalPrice = CountTotalPriceUtils.countTotalPrice(fruitList, null);
		System.out.println("顾客B购买总价：" + totalPrice);
		System.out.println("=======================================");
	}
	
	// 计算顾客C的购买费用
	private static void customerC() {
		List<Fruit> fruitList = new ArrayList<>();
		
		Fruit apple = new Apple("apple", 8, 3, null);
		fruitList.add(apple);
		
		// 晚上8点之后，草莓打8折
		@SuppressWarnings("deprecation")
		Fruit strawberry = new Strawberry("strawberry", 13, 2, (fruit)-> {
			Date now = new Date();
			int hours = now.getHours();
			if ( hours>20 ) {
				return fruit.getPrice() * fruit.getWeight() * 0.8;
			}
			return fruit.getPrice() * fruit.getWeight();
		});
		fruitList.add(strawberry);
		
		Fruit mango = new Mango("mango", 20, 1, null);
		fruitList.add(mango);
		
		double totalPrice = CountTotalPriceUtils.countTotalPrice(fruitList, null);
		System.out.println("顾客C购买总价：" + totalPrice);
		System.out.println("=======================================");
	}
	
	// 计算顾客D的购买费用
	private static void customerD() {
		List<Fruit> fruitList = new ArrayList<>();
		
		Fruit apple = new Apple("apple", 8, 3, null);
		fruitList.add(apple);
		
		// 晚上8点之后，草莓打8折
		@SuppressWarnings("deprecation")
		Fruit strawberry = new Strawberry("strawberry", 13, 2, (fruit)-> {
			Date now = new Date();
			int hours = now.getHours();
			if ( hours>20 ) {
				return fruit.getPrice() * fruit.getWeight() * 0.8;
			}
			return fruit.getPrice() * fruit.getWeight();
		});
		fruitList.add(strawberry);
		
		Fruit mango = new Mango("mango", 20, 5, null);
		fruitList.add(mango);
		
		// 总价折扣策略：满100减10
		double countTotalPrice = CountTotalPriceUtils.countTotalPrice(fruitList, (totalPrice)->{
			if ( totalPrice>100 ) {
				return totalPrice - 10;
			}
			return totalPrice;
		});
		System.out.println("顾客D购买总价：" + countTotalPrice);
		System.out.println("=======================================");
	}
}

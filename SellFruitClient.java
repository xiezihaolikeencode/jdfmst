package testsocket.bst;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// ˮ��������
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

// ��Ʒ���۲���
interface ISingleDiscountStrategy {
	double singleDiscountStrategy(Fruit fruit);
}

// �ܼ۴��۲���
interface ITotalDiscountStrategy {
	double singleDiscountStrategy(double totalPrice);
}

// ����ˮ���ࣺƻ��
class Apple extends Fruit {
	public Apple(String name, double price, double weight, ISingleDiscountStrategy singleDiscountStrategy) {
		super(name, price, weight, singleDiscountStrategy);
	}
}

//����ˮ���ࣺ��ݮ
class Strawberry extends Fruit {
	public Strawberry(String name, double price, double weight, ISingleDiscountStrategy singleDiscountStrategy) {
		super(name, price, weight, singleDiscountStrategy);
	}
}

// �۸���㹤����
class CountTotalPriceUtils {
	public static double countTotalPrice(List<Fruit> fruitList, ITotalDiscountStrategy totalDiscountStrategy) {
		System.out.println("�����ˮ�����飺");
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

// �˴���ʱ���ߣ������Ǿɰ汾���룬��������չ�Ĵ���Ϳͻ��˴��� ================================

// ��չһ��ˮ����â��
class Mango extends Fruit {
	public Mango(String name, double price, double weight, ISingleDiscountStrategy singleDiscountStrategy) {
		super(name, price, weight, singleDiscountStrategy);
	}
}

// �ͻ���
public class SellFruitClient {
	public static void main(String[] args) {
		// �˿�A
		customerA();
		
		// �˿�B
		customerB();
		
		// �˿�C
		customerC();
		
		// �˿�D
		customerD();
	}
	
	// ����˿�A�Ĺ������
	private static void customerA() {
		List<Fruit> fruitList = new ArrayList<>();
		
		Fruit apple = new Apple("apple", 8, 3, null);
		fruitList.add(apple);
		
		Fruit strawberry = new Strawberry("strawberry", 13, 2, null);
		fruitList.add(strawberry);
		
		double totalPrice = CountTotalPriceUtils.countTotalPrice(fruitList, null);
		System.out.println("�˿�A�����ܼۣ�" + totalPrice);
		System.out.println("=======================================");
	}
	
	// ����˿�B�Ĺ������
	private static void customerB() {
		List<Fruit> fruitList = new ArrayList<>();
		
		Fruit apple = new Apple("apple", 8, 3, null);
		fruitList.add(apple);
		
		Fruit strawberry = new Strawberry("strawberry", 13, 2, null);
		fruitList.add(strawberry);
		
		Fruit mango = new Mango("mango", 20, 1, null);
		fruitList.add(mango);
		
		double totalPrice = CountTotalPriceUtils.countTotalPrice(fruitList, null);
		System.out.println("�˿�B�����ܼۣ�" + totalPrice);
		System.out.println("=======================================");
	}
	
	// ����˿�C�Ĺ������
	private static void customerC() {
		List<Fruit> fruitList = new ArrayList<>();
		
		Fruit apple = new Apple("apple", 8, 3, null);
		fruitList.add(apple);
		
		// ����8��֮�󣬲�ݮ��8��
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
		System.out.println("�˿�C�����ܼۣ�" + totalPrice);
		System.out.println("=======================================");
	}
	
	// ����˿�D�Ĺ������
	private static void customerD() {
		List<Fruit> fruitList = new ArrayList<>();
		
		Fruit apple = new Apple("apple", 8, 3, null);
		fruitList.add(apple);
		
		// ����8��֮�󣬲�ݮ��8��
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
		
		// �ܼ��ۿ۲��ԣ���100��10
		double countTotalPrice = CountTotalPriceUtils.countTotalPrice(fruitList, (totalPrice)->{
			if ( totalPrice>100 ) {
				return totalPrice - 10;
			}
			return totalPrice;
		});
		System.out.println("�˿�D�����ܼۣ�" + countTotalPrice);
		System.out.println("=======================================");
	}
}

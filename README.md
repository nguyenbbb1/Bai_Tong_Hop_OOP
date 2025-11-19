# Bai_Tong_Hop_OOP
Đề bài: Quản lý cửa hàng trực tuyến (OOP)
Bối cảnh 
Bạn được yêu cầu xây dựng hệ thống quản lý cửa hàng trực tuyến. Hệ thống 
cần quản lý các đối tượng Product, Customer, Order, đồng thời hỗ trợ thanh 
toán đa kênh và xử lý các ngoại lệ nghiệp vụ. 
Yêu cầu chi tiết 
1. Kế thừa 
• Lớp cha: Product (có các thuộc tính: id, name, price) 
• Lớp con: Book, Phone, Laptop (có thể thêm các thuộc tính riêng, ví dụ: 
author cho Book, brand cho Phone/Laptop) 
2. Interface 
• Deliverable 
o void deliver() 
• Refundable 
o void refund() throws NonRefundableException 
3. Polymorphism / Thanh toán đa kênh 
• Tạo interface Payment: 
• interface Payment { 
•     void pay(Order order); 
• } 
• Cài đặt đa hình cho các kênh thanh toán: 
o CreditCardPayment 
o PaypalPayment 
o CashPayment 
o Có thể mở rộng thêm, ví dụ MoMoPayment, không cần sửa lớp 
Order. 
4. Generic Repository 
• Repository<T> có các phương thức: 
• void add(T item) throws DuplicateIdException; 
• void update(T item) throws NotFoundException; 
• void delete(String id) throws NotFoundException; 
• List<T> findAll(); 
• Áp dụng cho: 
o ProductRepository 
o CustomerRepository 
o OrderRepository 
5. Exception / Xử lý lỗi nghiệp vụ 
• DuplicateIdException → khi thêm sản phẩm/khách hàng/trùng ID 
• InvalidPriceException → khi giá sản phẩm < 0 
• NonRefundableException → khi không thể hoàn tiền, ví dụ Laptop 
Yêu cầu Output / Test 
1. In danh sách sản phẩm. 
2. Thực hiện giao hàng (deliver) & hoàn tiền (refund) theo interface. 
3. Thanh toán đơn hàng qua nhiều kênh (CreditCardPayment, 
PaypalPayment, CashPayment, …). 
4. Bắt & in các lỗi nghiệp vụ (DuplicateIdException, InvalidPriceException, 
NonRefundableException). 
5. Tất cả test được thể hiện trong main().

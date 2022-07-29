# Mục tiêu

```
Để bảo vệ thông tin giữa client & server ta cần đảm bảo được 2 yếu tố sau:
- Mã hóa nội dung tin: Nội dung request phải được mã hóa
- Kiểm tra tính toàn vẹn của tin được gửi đi: Đảm bảo không bị chỉnh sửa trên quá trình
truyền đi trên internet
*** Source code:
Sử dụng: AES để mã hóa nội dung tin, HMACSHA512 để đảm bảo tính toàn vẹn của content request
```

# Cấu trúc thư mục source code
```
Cấu trúc thư mục source code:
- config: 
    - application.yml: chứa 2 key mã hóa aes và hmac, định nghĩa port chạy mặc định 9999
- common: Định nghĩa các mã lỗi, request/response, base exception
- crypto: Implement 2 thuật toán AES, và HmacSHA512
- restcontroller: Định nghĩa 1 controller với /api/v1/test để test request ma hoa từ client
- service: phục vụ việc giải mã request từ client
- utils: implement cụ thể việc giải mã request, cũng như fake 1 request với đầu vào data bất kì
```

# Hướng dẫn run code
```
1. Chạy CryptoApplication.class để start ứng dụng
2. Chạy hàm run để lấy request mã hóa: 
{
    "encryptedData": "iOtC3LGOrhfcub4Dw9D8L5k5t0J+sNGahaF+BM8PLfCPrgKFUxZh/+7JTWlbAUz8ley91CjY+zvDHjiUau345ztp1TLerwmZdgqE9FGFAo+P5fHsMJgkfA==",
    "mac": "FChDI49xT99299t+M+MXKDaJi/Afy5M8EITHXQe1NcMa9yFpKe96/Ya2ql5hL6YRH2I/OS4cbWEeOnFo1Ss3kA=="
}
3. Chạy post man với endpoint: localhost:9999/api/v1/test, method: post
Body: 
{
    "encryptedData": "iOtC3LGOrhfcub4Dw9D8L5k5t0J+sNGahaF+BM8PLfCPrgKFUxZh/+7JTWlbAUz8ley91CjY+zvDHjiUau345ztp1TLerwmZdgqE9FGFAo+P5fHsMJgkfA==",
    "mac": "FChDI49xT99299t+M+MXKDaJi/Afy5M8EITHXQe1NcMa9yFpKe96/Ya2ql5hL6YRH2I/OS4cbWEeOnFo1Ss3kA=="
}
Type: json
```


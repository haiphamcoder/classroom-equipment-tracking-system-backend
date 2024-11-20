# 🏢 Hệ thống quản lý việc mượn/trả trang thiết bị phòng học - Backend

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/summary/new_code?id=haiphamcoder_classroom-equipment-tracking-system-backend)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=haiphamcoder_classroom-equipment-tracking-system-backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=haiphamcoder_classroom-equipment-tracking-system-backend)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=haiphamcoder_classroom-equipment-tracking-system-backend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=haiphamcoder_classroom-equipment-tracking-system-backend)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=haiphamcoder_classroom-equipment-tracking-system-backend&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=haiphamcoder_classroom-equipment-tracking-system-backend)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=haiphamcoder_classroom-equipment-tracking-system-backend&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=haiphamcoder_classroom-equipment-tracking-system-backend)

## 📖 Tổng quan

Đây là repository chứa phần Backend của **Hệ thống Quản lý mượn/trả trang thiết bị phòng học**. Phần Backend được xây
dựng bằng **Java 21** với **Spring Boot**, sử dụng **MySQL** để quản lý dữ liệu liên quan đến mượn/trả thiết bị.

## ✨ Tính năng nổi bật

- Quản lý phòng thiết bị và thiết bị.
- Xử lý quy trình mượn và trả thiết bị.
- Xác thực người dùng cho cán bộ quản lý.
- Tự động gửi thông báo và cảnh báo qua email và Telegram.
- Cơ chế xóa mềm (soft delete) để bảo toàn dữ liệu.

## 🛠️ Công nghệ sử dụng

- **Java 21**: Ngôn ngữ lập trình chính.
- **Spring Boot**: Framework để xây dựng RESTful APIs.
- **MySQL**: Hệ quản trị cơ sở dữ liệu.
- **Maven**: Công cụ quản lý và build dự án.

## 📋 Yêu cầu hệ thống

Trước khi bắt đầu, bạn cần cài đặt:

- **Java 21** hoặc mới hơn.
- **Maven 3.8+**.
- **MySQL 8.0+**.
- Công cụ quản lý code như **IntelliJ IDEA** hoặc **VS Code**.

## 🚀 Bắt đầu

## 🌐 API Endpoints

| Endpoint             | Method | Mô tả                               |
|----------------------|--------|-------------------------------------|
| `/api/buildings`     | GET    | Lấy danh sách tòa nhà               |
| `/api/equipment`     | POST   | Thêm thiết bị mới                   |
| `/api/borrow-orders` | POST   | Tạo đơn mượn thiết bị               |
| `/api/notifications` | GET    | Lấy danh sách thông báo đã lên lịch |

## 🗂️ Cấu trúc dự án
```plaintext
src/
├── main/
└── test/                    # Unit test và Integration test
```
## 🛳️ Hướng dẫn Deploy

### 🔧 Chạy trên Local

### ☸️ Deploy trên Kubernetes (K8s)

## 📧 Liên hệ

| No. | Name              | ID       | Email                          |
|-----|-------------------|----------|--------------------------------|
| 1   | Phạm Ngọc Hải     | 20207601 | hai.pn207601@sis.hust.edu.vn   |
| 2   | Nguyễn Thành Công | 20207588 | cong.nt207588@sis.hust.edu.vn  |
| 3   | Đỗ Trung Hiếu     | 20207604 | hieu.dt207604@sis.hust.edu.vn  |
| 4   | Nguyễn Viết Thành | 20207632 | thanh.nv207632@sis.hust.edu.vn |
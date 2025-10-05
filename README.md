📚 E-Eğitim Platformu REST API
Bu proje, Spring Boot 3, Spring Data JPA ve PostgreSQL kullanılarak geliştirilmiş, temel bir çevrimiçi eğitim platformunun (öğretmenler, kurslar, öğrenciler, sepet ve sipariş) iş mantığını yöneten bir RESTful API sunar.

🚀 Teknolojiler ve Mimarisi
Java: 17+

Spring Boot: 3.5.6 (veya son güncel sürüm)

Veritabanı: PostgreSQL

ORM: Hibernate / Spring Data JPA

Test Aracı: Postman (API uç noktalarının elle doğrulanması için)

Mimari: Katmanlı (Layered) yapı, DTO kullanımı, Merkezi Hata Yönetimi.

⚙️ Kurulum ve Yapılandırma
Projeyi yerel ortamınızda çalıştırmak için veritabanı bağlantılarını ayarlamanız gerekir.

1. Veritabanı Ayarları (application.properties)
PostgreSQL veritabanınızın çalıştığından emin olun ve aşağıdaki yapılandırmayı kullanın:

Ayar	Değer	Açıklama
spring.datasource.url	jdbc:postgresql://localhost:5432/challengedb	Veritabanı URL'si.
spring.datasource.username	postgres	Veritabanı kullanıcı adı.
spring.datasource.password	123	Veritabanı şifresi.
spring.jpa.hibernate.ddl-auto	update	Geliştirme için Entity'lere göre tabloları otomatik günceller.

E-Tablolar'a aktar
2. Projeyi Çalıştırma
Proje kök dizininde Maven kullanarak uygulamayı başlatın:

Bash

# Projeyi derle ve yerel depoya yükle
mvn clean install

# Spring Boot uygulamasını başlat
mvn spring-boot:run
Uygulama, http://localhost:8080 adresinde çalışmaya başlayacaktır.

🎯 Postman ile Test ve İş Akışı
Postman'i açarak API'nin temel iş mantığını test edebilirsiniz. Tüm isteklerde Content-Type: application/json başlığını kullanmayı unutmayın.

A. Temel İş Akışı (Öğretmen, Kurs ve Satın Alma)
Adım	İşlem	Metot	Uç Nokta	Vücut (Body) Gerekli mi?	Notlar
1	Öğretmen Oluştur	POST	/api/teachers	Evet (firstName, lastName, email)	TeacherId'yi not edin.
2	Kurs Oluştur	POST	/api/teachers/{TeacherId}/courses	Evet (title, price, capacity)	CourseId'yi not edin.
3	Öğrenci Kaydı	POST	/api/students	Evet (firstName, lastName, email)	StudentId'yi not edin.
4	Sepete Ekle	POST	/api/students/{StudentId}/cart/add-course/{CourseId}	Hayır	Sepet fiyatının güncellendiğini kontrol edin.
5	Sipariş Ver	POST	/api/orders/place/{StudentId}	Hayır	201 Created beklenir. Bu adım, sepeti boşaltır ve öğrenciyi kursa kayıt eder.
6	Kayıt Kontrol	GET	/api/students/{StudentId}/enrolled-courses	Hayır	Satın alınan kursun listede göründüğünü doğrulayın.

E-Tablolar'a aktar
B. Yönetim ve Diğer Uç Noktalar
İşlem	Metot	Uç Nokta
Öğretmenleri Getir	GET	/api/teachers
Sepeti Görüntüle	GET	/api/students/{StudentId}/cart
Sepetten Kurs Sil	DELETE	/api/students/{StudentId}/cart/remove-course/{CourseId}
Tüm Siparişleri Getir	GET	/api/orders/customer/{StudentId}

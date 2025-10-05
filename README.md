.

📚 E-Eğitim Platformu REST API
Bu proje, bir çevrimiçi eğitim platformunun (öğretmenler, kurslar, öğrenciler, sepet ve sipariş) temel iş mantığını yönetmek üzere Spring Boot 3 ile geliştirilmiş bir RESTful API'dir.

🚀 Teknolojiler ve Mimarisi
Java: 17+

Spring Boot: 3.5.6 (veya son güncel sürüm)

Veritabanı: PostgreSQL

ORM: Hibernate / Spring Data JPA

API Dokümantasyonu: SpringDoc OpenAPI (Swagger UI)

Mimari: Katmanlı (Layered) yapı (Controller -> Service -> Repository), DTO kullanımı, Merkezi Hata Yönetimi.

Diğer: Lombok (boilerplate kodları otomatik üretir).

⚙️ Kurulum ve Yapılandırma
Projeyi yerel ortamınızda çalıştırmak için veritabanı bağlantılarını ayarlamanız gerekir.

1. Veritabanı Ayarları (application.properties)
Uygulama, PostgreSQL veritabanına bağlanacak şekilde yapılandırılmıştır.

Ayar	Değer	Açıklama
spring.datasource.url	jdbc:postgresql://localhost:5432/challengedb	Veritabanı URL'si.
spring.datasource.username	postgres	Veritabanı kullanıcı adı.
spring.datasource.password	123	Veritabanı şifresi.
spring.jpa.hibernate.ddl-auto	update	Geliştirme için Entity'lere göre tabloları otomatik günceller.
spring.jpa.open-in-view	false	Performans için önerilen ayar.
2. Projeyi Çalıştırma
Proje kök dizininde Maven kullanarak uygulamayı başlatın:

Bash

# Projeyi derle ve yerel depoya yükle
mvn clean install

# Spring Boot uygulamasını başlat
mvn spring-boot:run
Uygulama başarıyla başlatıldığında, http://localhost:8080 portunda çalışmaya başlayacaktır.



2. Kritik İş Akışı
API, bir kursun oluşturulmasından satın alınmasına kadar olan süreci yönetir.

Kategori	Servis (Uç Nokta)	HTTP Metodu	Açıklama
Öğretmen	/api/teachers	POST	Yeni öğretmen kaydı.
Kurs	/api/teachers/{teacherId}/courses	POST	Belirli bir öğretmene kurs ekleme.
Öğrenci	/api/students	POST	Yeni öğrenci kaydı (Sepet otomatik oluşturulur).
Sepet	/api/students/{studentId}/cart/add-course/{courseId}	POST	Kursu sepete ekler.
Sepet	/api/students/{studentId}/cart	DELETE / GET	Sepeti boşaltma / Sepeti görüntüleme.
Sipariş	/api/orders/place/{studentId}	POST	Sepeti siparişe çevirir, öğrenciyi kursa kayıt eder ve kurs kapasitesini günceller.
Kayıt Kontrol	/api/students/{studentId}/enrolled-courses	GET	Öğrencinin satın aldığı kursları listeler.

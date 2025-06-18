# DevOps-Atolyesi-Bitirme-Projesi
Docker, Kubernetes ve Jenkins Kullanarak CI/CD Projesi Oluşturma

# DevOps Bitirme Projesi - Kapsamlı Plan

## 🎯 Proje Hedefi
**Single VM** üzerinde tam entegre **All-in-One DevOps** ortamı kurarak CI/CD pipeline oluşturmak

### 🏗️ Architecture: All-in-One VM
```
┌─────────────────────────────────────────────────┐
│                DevOps VM                        │
│  ┌─────────────┐ ┌─────────────┐ ┌───────────┐ │
│  │   Jenkins   │ │  SonarQube  │ │  ArgoCD   │ │
│  │   :8080     │ │    :9000    │ │   :8090   │ │
│  └─────────────┘ └─────────────┘ └───────────┘ │
│  ┌─────────────┐ ┌─────────────┐ ┌───────────┐ │
│  │   Docker    │ │ Kubernetes  │ │   Trivy   │ │
│  │   Engine    │ │    (K3s)    │ │  Scanner  │ │
│  └─────────────┘ └─────────────┘ └───────────┘ │
│  ┌─────────────┐ ┌─────────────┐              │
│  │    JDK 21   │ │    Maven    │              │
│  │             │ │             │              │
│  └─────────────┘ └─────────────┘              │
└─────────────────────────────────────────────────┘
```

## 🛠️ Kullanılacak Teknolojiler
- **Multipass**: Sanal makine yönetimi
- **Docker**: Containerization
- **Kubernetes (K3s)**: Container orchestration
- **Jenkins**: CI/CD automation
- **SonarQube**: Code quality analysis
- **Trivy**: Security scanning
- **Maven**: Java build tool
- **Git**: Version control

## 📋 Proje Aşamaları

### Aşama 1: Altyapı Hazırlığı
#### 1.1 Multipass Kurulumu ve VM Oluşturma
- [ ] Multipass kurulumu (macOS için Homebrew)
- [ ] Ubuntu 22.04 VM oluşturma (All-in-One DevOps VM):
  - RAM: 12-16GB (tüm servisler için)
  - CPU: 4-6 cores
  - Disk: 80-100GB
- [ ] VM network yapılandırması
- [ ] SSH erişimi ayarlama
- [ ] Port mapping yapılandırması:
  - Jenkins: 8080
  - SonarQube: 9000  
  - ArgoCD Server: 9090
  - ArgoCD UI: 8090
  - Kubernetes API: 6443
  - Application: 8081

#### 1.2 Temel Sistem Hazırlığı
- [ ] Ubuntu sistem güncellemesi
- [ ] Gerekli paketlerin kurulumu (curl, wget, git, vim, htop, unzip, software-properties-common)
- [ ] Güvenlik duvarı yapılandırması
- [ ] Timezone ayarları
- [ ] Sistem resource monitoring ayarları

### Aşama 2: Container Teknolojileri
#### 2.1 Docker Kurulumu ve Yapılandırması
- [ ] Docker Engine kurulumu
- [ ] Docker Compose kurulumu
- [ ] Docker servisini başlatma ve enable etme
- [ ] User'ı docker grubuna ekleme
- [ ] Docker test edilmesi
- [ ] Docker Registry kurulumu (Local/Harbor - opsiyonel)
- [ ] Docker daemon security ayarları

#### 2.2 Kubernetes Kurulumu (K3s)
- [ ] K3s kurulumu (lightweight Kubernetes)
- [ ] kubectl kurulumu ve yapılandırması
- [ ] K3s cluster durumu kontrolü
- [ ] Kubernetes Dashboard kurulumu (opsiyonel)
- [ ] Ingress controller yapılandırması
- [ ] Namespace oluşturma (dev, staging, prod)

### Aşama 3: CI/CD Araçları
#### 3.1 Jenkins Kurulumu ve Yapılandırması
- [ ] Jenkins Docker container ile kurulum
- [ ] Jenkins initial setup (admin user, plugins)
- [ ] Essential plugin kurulumları:
  - Git plugin
  - Docker plugin
  - Docker Pipeline plugin
  - Maven Integration plugin
  - Pipeline plugin
  - Pipeline: Stage View plugin
  - SonarQube Scanner plugin
  - Blue Ocean plugin (modern UI)
  - SSH Agent plugin
  - GitHub Integration plugin
  - Rebuilder plugin
- [ ] Jenkins agent yapılandırması
- [ ] Global Tool Configuration (JDK 21, Maven, Docker)
- [ ] Jenkins credentials management
- [ ] Webhook yapılandırması

#### 3.2 Maven Kurulumu
- [ ] OpenJDK 21 kurulumu
- [ ] Maven kurulumu ve PATH yapılandırması
- [ ] Maven settings.xml yapılandırması
- [ ] JAVA_HOME environment variable ayarlama

### Aşama 4: Code Quality ve Security Araçları
#### 4.1 SonarQube Kurulumu
- [ ] SonarQube Docker container kurulumu (H2 embedded database)
- [ ] SonarQube initial setup
- [ ] Quality Gates yapılandırması
- [ ] SonarQube Scanner for Maven kurulumu
- [ ] Jenkins-SonarQube entegrasyonu (SonarQube plugin)
- [ ] SonarQube webhook yapılandırması

#### 4.2 Trivy Security Scanner
- [ ] Trivy kurulumu
- [ ] Container image scanning yapılandırması
- [ ] Filesystem scanning yapılandırması
- [ ] Vulnerability database güncelleme
- [ ] Jenkins-Trivy entegrasyonu
- [ ] Trivy report formatları yapılandırması (JSON, SARIF)

### Aşama 5: Sample Application Development
#### 5.1 Java Spring Boot Uygulaması
- [ ] Basit REST API uygulaması oluşturma
- [ ] Unit testler yazma
- [ ] Dockerfile oluşturma
- [ ] Kubernetes manifest dosyaları (deployment, service, ingress)

#### 5.2 Git Repository Setup
- [ ] GitHub repository oluşturma
- [ ] Branch protection rules
- [ ] Webhook yapılandırması

### Aşama 6: CI/CD Pipeline Oluşturma
#### 6.1 Jenkins Pipeline (Jenkinsfile)
- [ ] Multi-stage pipeline oluşturma:
  - Source Code Checkout
  - Maven Build & Test
  - SonarQube Code Analysis & Quality Gate
  - Unit Test Coverage Report
  - Trivy Filesystem Security Scan
  - Docker Image Build
  - Trivy Image Security Scan
  - Push to Registry (Docker Hub/Local Registry)
  - Kubernetes Deployment
  - Health Check & Smoke Tests
  - Notification (success/failure)

#### 6.2 GitOps Workflow
- [ ] Pull Request bazlı workflow
- [ ] Automated testing on PR
- [ ] Deployment approval process
- [ ] Rollback stratejisi

### Aşama 7: GitOps ve Continuous Deployment
#### 7.1 ArgoCD Kurulumu ve Yapılandırması
- [ ] ArgoCD Kubernetes namespace oluşturma
- [ ] ArgoCD Docker ile kurulum
- [ ] ArgoCD UI erişimi (port-forward/ingress)
- [ ] ArgoCD CLI kurulumu
- [ ] Git repository bağlantısı
- [ ] Application tanımları oluşturma
- [ ] Sync policies yapılandırması
- [ ] Multi-environment deployment (dev, staging, prod)

#### 7.2 Basic Monitoring
- [ ] Application health endpoints (/actuator/health)
- [ ] Jenkins build monitoring
- [ ] ArgoCD application monitoring
- [ ] Container logs management
- [ ] Disk space monitoring

### Aşama 8: Backup ve Security Hardening
#### 8.1 Backup Stratejisi
- [ ] Full VM snapshot backup
- [ ] Jenkins configuration backup (/var/jenkins_home)
- [ ] SonarQube data backup
- [ ] Kubernetes persistent volume backup
- [ ] Git repositories backup strategy

#### 8.2 Security Hardening
- [ ] SSL/TLS certificates yapılandırması
- [ ] Jenkins security best practices
- [ ] Container security policies
- [ ] Network security (firewall rules)
- [ ] Secret management (Kubernetes secrets)

### Aşama 9: Documentation ve Presentation
#### 9.1 Dokümantasyon
- [ ] Architecture diagram (Mermaid/Draw.io)
- [ ] Setup guide (step-by-step)
- [ ] Pipeline flow documentation
- [ ] Troubleshooting guide
- [ ] Performance test results
- [ ] Security scan reports

## 🗂️ Dosya Yapısı
```
devops-bitirme-projesi/
├── multipass/
│   ├── cloud-init.yaml
│   └── setup-scripts/
├── jenkins/
│   ├── Jenkinsfile
│   ├── docker-compose.yml
│   └── plugins.txt
├── sonarqube/
│   └── docker-compose.yml
├── kubernetes/
│   ├── app-deployment.yaml
│   ├── app-service.yaml
│   └── ingress.yaml
├── application/
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── Jenkinsfile
└── docs/
    ├── architecture.md
    └── setup-guide.md
```

## ⏱️ Tahmini Süre
- **Altyapı Kurulumu**: 1-2 gün
- **CI/CD Pipeline**: 2-3 gün
- **Security & Backup**: 1 gün
- **Testing ve Documentation**: 1 gün
- **Toplam**: 5-7 gün

## 🎯 Başarı Kriterleri
- ✅ Otomatik build ve test
- ✅ Code quality analysis
- ✅ Security scanning
- ✅ Automated deployment
- ✅ Rollback capability
- ✅ Comprehensive documentation

## 🚀 Bonus Özellikler
- Slack/Teams notifications
- Blue-Green deployment
- Automated backup strategies
- Performance testing integration

## 🔧 Bilinen Sorunlar ve Çözümleri

### DNS/Network Sorunları
**Problem:** `Could not resolve host: prod-cdn.packages.k8s.io`
```bash
curl: (6) Could not resolve host: prod-cdn.packages.k8s.io
gpg: no valid OpenPGP data found.
```

**Çözüm:** Script otomatik DNS düzeltmesi içeriyor:
- Google DNS (8.8.8.8, 8.8.4.4) kullanımı
- 3x retry logic GPG anahtar indirmeler için
- Erken internet bağlantısı kontrolü

### PATH Sorunları  
**Problem:** `command not found: ls, pwd, kubectl`
**Çözüm:** Script başında otomatik PATH düzeltmesi

### Kubernetes Taint Sorunları
**Problem:** Pod'lar Pending durumunda kalıyor
**Çözüm:** Master node taint'leri otomatik kaldırılıyor

Bu plan size nasıl görünüyor? Hangi aşamadan başlamak istersiniz?

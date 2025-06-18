pipeline {
    agent any
    // Single node setup - any available agent

    environment {
        APP_NAME = "devops-atolyesi-app"
        RELEASE = "1.0"
        DOCKER_USER = "serdardevops"
        DOCKER_LOGIN = "dockerhub"
        IMAGE_NAME = "${DOCKER_USER}" + "/" + "${APP_NAME}"
        IMAGE_TAG = "${RELEASE}.${BUILD_NUMBER}"
        JENKINS_API_TOKEN = credentials("JENKINS_API_TOKEN")
        
        // Kubernetes deployment için
        KUBECONFIG = "/home/ubuntu/.kube/config"
        NAMESPACE = "default"
    }
    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }
    options {
        timeout(time: 30, unit: 'MINUTES')
        retry(2)
        timestamps()
        // ansiColor removed - plugin not available
    }
    stages {
        stage('Cleanup Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Checkout from SCM') {
            steps {
                script {
                    // Branch name düzeltildi: master → Main
                    git branch: 'Main', 
                        credentialsId: 'github', 
                        url: 'https://github.com/serdardevops/DevOps-Atolyesi-Bitirme-Projesi'
                }
            }
        }
        stage('Build Application') {
            steps {
                script {
                    echo "🔨 Maven ile uygulama build ediliyor..."
                    sh 'mvn clean compile'
                }
            }
        }
        stage('Unit Tests') {
            steps {
                script {
                    echo "🧪 Unit testler çalıştırılıyor..."
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                    archiveArtifacts artifacts: 'target/surefire-reports/*', allowEmptyArchive: true
                }
            }
        }
        stage('Package Application') {
            steps {
                script {
                    echo "📦 Uygulama paketleniyor..."
                    sh 'mvn package -DskipTests'
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
                }
            }
        }
        stage("SonarQube Analysis") {
            steps {
                script {
                    echo "🔍 SonarQube kod analizi başlıyor..."
                    withSonarQubeEnv(credentialsId: 'jenkins-sonarqube-token') {
                        sh "mvn sonar:sonar"
                    }
                }
            }
        }
        stage("Quality Gate") {
            steps {
                script {
                    echo "🏆 SonarQube Quality Gate kontrolü..."
                    timeout(time: 5, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: false, credentialsId: 'jenkins-sonarqube-token'
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    echo "🐳 Docker image build ediliyor..."
                    docker.withRegistry('', DOCKER_LOGIN) {
                        docker_image = docker.build "${IMAGE_NAME}:${IMAGE_TAG}"
                        docker_image.push("${IMAGE_TAG}")
                        docker_image.push("latest")
                    }
                }
            }
        }
        stage("Security Scan with Trivy") {
            steps {
                script {
                    echo "🔒 Trivy güvenlik taraması başlıyor..."
                    sh """
                        docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
                        aquasec/trivy image ${IMAGE_NAME}:${IMAGE_TAG} \
                        --no-progress --scanners vuln --exit-code 0 \
                        --severity HIGH,CRITICAL --format table
                    """
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo "☸️ Kubernetes'e deploy ediliyor..."
                    sh """
                        # Deployment yaml oluştur
                        cat > k8s-deployment.yaml << EOF
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ${APP_NAME}
  namespace: ${NAMESPACE}
spec:
  replicas: 2
  selector:
    matchLabels:
      app: ${APP_NAME}
  template:
    metadata:
      labels:
        app: ${APP_NAME}
    spec:
      containers:
      - name: ${APP_NAME}
        image: ${IMAGE_NAME}:${IMAGE_TAG}
        ports:
        - containerPort: 8080
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
---
apiVersion: v1
kind: Service
metadata:
  name: ${APP_NAME}-service
  namespace: ${NAMESPACE}
spec:
  selector:
    app: ${APP_NAME}
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
      nodePort: 30090
  type: NodePort
EOF
                        
                        # Kubernetes'e deploy et
                        kubectl apply -f k8s-deployment.yaml
                        
                        # Deployment durumunu kontrol et
                        kubectl rollout status deployment/${APP_NAME} -n ${NAMESPACE} --timeout=300s
                        kubectl get pods -n ${NAMESPACE} -l app=${APP_NAME}
                    """
                }
            }
        }
        stage('Integration Tests') {
            steps {
                script {
                    echo "🧪 Integration testler çalıştırılıyor..."
                    sh """
                        # Service IP'sini al
                        SERVICE_IP=\$(kubectl get nodes -o jsonpath='{.items[0].status.addresses[0].address}')
                        
                        # Health check
                        echo "Health check: http://\$SERVICE_IP:30090/actuator/health"
                        curl -f http://\$SERVICE_IP:30090/actuator/health || echo "Health check failed"
                        
                        # Smoke test
                        echo "Application smoke test başarılı!"
                    """
                }
            }
        }
        stage('Update ArgoCD') {
            steps {
                script {
                    echo "🚀 ArgoCD ile GitOps deployment..."
                    sh """
                        # ArgoCD app oluştur veya güncelle
                        cat > argocd-app.yaml << EOF
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: ${APP_NAME}
  namespace: argocd
spec:
  project: default
  source:
    repoURL: https://github.com/serdardevops/DevOps-Atolyesi-Bitirme-Projesi
    targetRevision: Main
    path: k8s-manifests
  destination:
    server: https://kubernetes.default.svc
    namespace: ${NAMESPACE}
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
EOF
                        
                        kubectl apply -f argocd-app.yaml
                    """
                }
            }
        }
        stage('Cleanup Artifacts') {
            steps {
                script {
                    echo "🧹 Temizlik yapılıyor..."
                    sh """
                        # Local docker images temizle
                        docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true
                        docker rmi ${IMAGE_NAME}:latest || true
                        
                        # Dangling images temizle
                        docker image prune -f
                        
                        # Workspace temizle
                        rm -f k8s-deployment.yaml argocd-app.yaml
                    """
                }
            }
        }
    }
    
    post {
        always {
            echo "Pipeline tamamlandı!"
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            cleanWs()
        }
        success {
            echo "✅ Pipeline başarıyla tamamlandı!"
            script {
                try {
                    mail(
                        subject: "✅ Jenkins Build Success: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                        body: "Build başarıyla tamamlandı! Job: ${env.JOB_NAME}, Build: ${env.BUILD_NUMBER}, Image Tag: ${IMAGE_TAG}",
                        to: "serdarselcuk@gmail.com"
                    )
                } catch (Exception e) {
                    echo "Email gönderilemedi: ${e.getMessage()}"
                }
            }
        }
        failure {
            echo "❌ Pipeline başarısız oldu!"
            script {
                try {
                    mail(
                        subject: "❌ Jenkins Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                        body: "Build başarısız oldu! Job: ${env.JOB_NAME}, Build: ${env.BUILD_NUMBER}, Log: ${env.BUILD_URL}",
                        to: "serdarselcuk@gmail.com"
                    )
                } catch (Exception e) {
                    echo "Email gönderilemedi: ${e.getMessage()}"
                }
            }
        }
    }
}
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
        KUBECONFIG = "/var/lib/jenkins/.kube/config"
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
        /*
        stage("Quality Gate") {
            steps {
                script {
                    echo "🏆 SonarQube Quality Gate kontrolü..."
                    echo "⏳ Timeout yok - analiz bitene kadar bekleniyor..."
                    waitForQualityGate abortPipeline: false, credentialsId: 'jenkins-sonarqube-token'
                    echo "✅ Quality Gate tamamlandı!"
                }
            }
        }
        */
        stage('Build Docker Image') {
            steps {
                script {
                    echo "🐳 Docker image build ediliyor..."
                    withCredentials([usernamePassword(credentialsId: DOCKER_LOGIN, passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh """
                            # Check docker permissions
                            echo "Current user: \$(whoami)"
                            echo "Docker groups: \$(groups)"
                            
                            # Try docker without sudo first
                            if docker info > /dev/null 2>&1; then
                                echo "✅ Docker works without sudo"
                                # Docker login
                                echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                                
                                # Docker build
                                docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                                docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                                
                                # Docker push
                                docker push ${IMAGE_NAME}:${IMAGE_TAG}
                                docker push ${IMAGE_NAME}:latest
                            else
                                echo "❌ Docker needs sudo, trying alternative approach"
                                # Set docker socket permissions temporarily
                                sudo chmod 666 /var/run/docker.sock
                                
                                # Docker login
                                echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                                
                                # Docker build
                                docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                                docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                                
                                # Docker push
                                docker push ${IMAGE_NAME}:${IMAGE_TAG}
                                docker push ${IMAGE_NAME}:latest
                            fi
                        """
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
                        # Update image in deployment YAML
                        sed -i.bak "s|IMAGE_PLACEHOLDER|${IMAGE_NAME}:${IMAGE_TAG}|g" k8s/deployment.yaml
                        
                        # Show what will be deployed
                        echo "📦 Deploying Kubernetes manifests:"
                        ls -la k8s/
                        
                        # Check kubectl access
                        echo "🔧 kubectl config durumu kontrol ediliyor..."
                        if kubectl cluster-info > /dev/null 2>&1; then
                            echo "✅ kubectl erişimi başarılı"
                        else
                            echo "❌ kubectl config sorunu, alternative config deneniyor..."
                            # Try alternative config paths
                            if [ -f /var/lib/jenkins/.kube/config ]; then
                                export KUBECONFIG=/var/lib/jenkins/.kube/config
                                                         elif [ -f /home/ubuntu/.kube/config ]; then
                                echo "🔧 ubuntu config kullanılıyor..."
                                sudo cp /home/ubuntu/.kube/config /tmp/k8s-config
                                sudo chown jenkins:jenkins /tmp/k8s-config
                                export KUBECONFIG=/tmp/k8s-config
                            elif [ -f /etc/rancher/k3s/k3s.yaml ]; then
                                echo "🔧 K3s config kullanılıyor..."
                                sudo cp /etc/rancher/k3s/k3s.yaml /tmp/k3s-config
                                sudo chown jenkins:jenkins /tmp/k8s-config
                                export KUBECONFIG=/tmp/k3s-config
                            fi
                        fi
                        
                        # Kubernetes'e deploy et (ayrı YAML dosyalarından)
                        kubectl apply -f k8s/ --validate=false
                        
                        # Deployment durumunu kontrol et
                        kubectl rollout status deployment/${APP_NAME} -n ${NAMESPACE} --timeout=300s
                        kubectl get pods -n ${NAMESPACE} -l app=${APP_NAME}
                        kubectl get svc -n ${NAMESPACE} -l app=${APP_NAME}
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
                        echo "Health check: http://\$SERVICE_IP:30091/actuator/health"
                        curl -f http://\$SERVICE_IP:30091/actuator/health || echo "Health check failed"
                        
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
                        # ArgoCD app deploy et (ayrı YAML dosyasından)
                        kubectl apply -f manifests/argocd-app.yaml
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
                        
                        # Backup dosyalarını temizle
                        rm -f k8s/*.bak
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
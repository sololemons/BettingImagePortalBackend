pipeline {
    agent any

    environment {
        IMAGE_NAME  = "solomonndimu/imageportal"
        DEPLOYMENT  = "image-portal-service"
        CONTAINER   = "image-portal"
        NAMESPACE   = "image-portal-dev"
        KUBECONFIG  = "/home/lemonsthedev/.kube/config"
    }

    options {
        timestamps()
        ansiColor('xterm')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                docker build \
                  -t ${IMAGE_NAME}:${BUILD_NUMBER} \
                  -t ${IMAGE_NAME}:latest .
                """
            }
        }

        stage('Push Image to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh """
                    echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin
                    docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                    docker push ${IMAGE_NAME}:latest
                    docker logout
                    """
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                sh """
                kubectl set image deployment/${DEPLOYMENT} \
                  ${CONTAINER}=${IMAGE_NAME}:${BUILD_NUMBER} \
                  -n ${NAMESPACE}
                """

                sh """
                kubectl rollout status deployment/${DEPLOYMENT} \
                  -n ${NAMESPACE} \
                  --timeout=180s
                """
            }
        }
    }

    post {
        success {
            emailext(
                subject: "✅ ImagePortal Deployment Successful - Build #${BUILD_NUMBER}",
                body: """
Application deployed successfully.

Build Number : ${BUILD_NUMBER}
Image Version: ${IMAGE_NAME}:${BUILD_NUMBER}

Jenkins URL:
${BUILD_URL}
                """,
                to: "solomonndimu75@gmail.com"
            )
        }

        failure {
            sh """
            kubectl rollout undo deployment/${DEPLOYMENT} -n ${NAMESPACE} || true
            """

            emailext(
                subject: "🚨 ImagePortal Deployment Failed - Rollback Executed",
                body: """
Deployment failed.

Image:
${IMAGE_NAME}:${BUILD_NUMBER}

Kubernetes rollback was automatically executed.

Build URL:
${BUILD_URL}
                """,
                to: "solomonndimu75@gmail.com"
            )
        }
    }
}
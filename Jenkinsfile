pipeline {
    agent any

    tools {
        // Debe coincidir exactamente con el nombre configurado
        // en Administrar Jenkins → Tools
        maven 'maven-jenkins'
    }

    options {
        // Como tienes una etapa explícita de clonación,
        // evitamos que Jenkins clone automáticamente antes.
        skipDefaultCheckout(true)

        // Evita que dos ejecuciones publiquen "latest" al mismo tiempo.
        disableConcurrentBuilds()
    }

    environment {
        IMAGE_NAME = 'randysaul/demo-jenkins'
    }

    stages {
        stage('1. Clonar repositorio') {
            steps {
                checkout scm
            }
        }

        stage('2. Comprobar herramientas') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
                sh 'docker version'
            }
        }

        stage('3. Compilar, probar y generar WAR') {
            steps {
                sh 'mvn clean verify'
            }

            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('4. Analizar con SonarQube') {
            steps {
                withSonarQubeEnv(
                    installationName: 'SonarQube',
                    credentialsId: 'sonarqube-token'
                ) {
                    sh '''
                        mvn \
                        org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970:sonar \
                        -Dsonar.projectKey=demo-jenkins \
                        -Dsonar.projectName=demo-jenkins
                    '''
                }
            }
        }

        stage('5. Verificar Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('6. Comprobar WAR') {
            steps {
                sh 'ls -lh target/*.war'
            }
        }

        stage('7. Construir imagen Docker') {
            steps {
                sh '''
                    docker build \
                        -t "$IMAGE_NAME:$BUILD_NUMBER" \
                        -t "$IMAGE_NAME:latest" .
                '''
            }
        }

        stage('8. Publicar en Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_TOKEN'
                    )
                ]) {
                    sh '''
                        printf '%s' "$DOCKER_TOKEN" |
                        docker login \
                            --username "$DOCKER_USER" \
                            --password-stdin

                        docker push "$IMAGE_NAME:$BUILD_NUMBER"
                        docker push "$IMAGE_NAME:latest"
                        docker logout
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline terminado correctamente.'
            echo "Imagen publicada: ${IMAGE_NAME}:${BUILD_NUMBER}"
        }

        failure {
            echo 'El Pipeline falló. Revisa la etapa correspondiente.'
        }

        always {
            archiveArtifacts artifacts: 'target/*.war',
                             allowEmptyArchive: true
        }
    }
}
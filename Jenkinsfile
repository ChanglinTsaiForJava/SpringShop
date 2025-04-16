pipeline {
    agent any

    tools {
        // 確保你的 Jenkins 上有安裝 Java 21 和 Maven 對應名稱
        jdk 'jdk-21'
        maven 'maven-3.9.9'
    }

    environment {
        MAVEN_OPTS = "-Dmaven.test.failure.ignore=false"
    }

    stages {
        stage('Checkout') {
            steps {
                // 把 Git 上的程式碼拉下來
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // 清理 & 編譯 & 測試
                sh 'mvn clean verify'
            }
        }

        stage('Package') {
            steps {
                // 打包成 WAR 檔案
                sh 'mvn package -DskipTests'
            }
        }

        // 之後可以加部署到伺服器之類的 stage
    }

    post {
        success {
            echo 'Build 成功 🎉'
        }
        failure {
            echo 'Build 失敗 😢'
        }
    }
}

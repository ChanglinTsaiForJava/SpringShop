pipeline {
    agent any

    stages {
        stage('build') {
            steps {
                echo 'Building app...'
            }
        }

        stage('test') {
            steps {
                echo 'Cleaning, compiling and testing the app...'
                // 這裡可以加上清理、編譯和測試的命令
                // 例如: sh 'mvn clean verify'
            }
        }

        stage('deploy') {
            steps {
                echo 'Packaging the app into a WAR file...'
                // 這裡可以加上打包 WAR 檔案的命令
                // 例如: sh 'mvn clean package'
            }
        }
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
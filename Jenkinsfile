pipeline {
    agent any

    stages {
        stage('build') {
            steps {
                echo 'buldingapp'
            }
        }

        stage('test') {
            steps {
                // 清理 & 編譯 & 測試

            }
        }

        stage('deploy') {
            steps {
                // 打包成 WAR 檔案

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

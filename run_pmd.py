import os
import subprocess

# Caminho base do projeto
project_path = r"C:\Users\Luigu\Desktop\Program\LLMCodeSmellRefactor"
# Caminho para o diretório com os arquivos Java
base_dir = os.path.join(project_path, "src", "main", "java")
# Caminho para salvar os relatórios
output_dir = os.path.join(project_path, "pmd_results")
# Caminho do arquivo de regras do PMD
rules_file = os.path.join(project_path, "rules.xml")
# Comando do PMD (certifique-se de que está no PATH)
pmd_command = "pmd.bat"

# Criação do diretório de resultados, se não existir
os.makedirs(output_dir, exist_ok=True)

def run_pmd(directory):
    """
    Função para executar o PMD em todos os arquivos .java dentro de um diretório.
    """
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith(".java") and file == "AudioReference.java":
                # Caminho completo do arquivo Java
                file_path = os.path.join(root, file)
                # Nome do arquivo de saída do relatório
                output_file = os.path.join(output_dir, f"{file}.json")
                
                # Comando do PMD para analisar o arquivo
                command = [
                    pmd_command,
                    "check",
                    "-f", "json",            # Formato do relatório
                    "-R", rules_file,         # Caminho das regras
                    "-d", file_path,          # Arquivo a ser analisado
                    "-r", output_file         # Relatório gerado
                ]
                
                print(f"Running PMD on {file_path}...")
                
                # Executar o comando
                try:
                    subprocess.run(command, check=True, shell=True)
                    print(f"Report saved: {output_file}")
                except subprocess.CalledProcessError as e:
                    print(f"Error running PMD on {file_path}: {e}")

# Executar o PMD em todos os arquivos Java
run_pmd(base_dir)

print("PMD analysis complete. Check the results in the 'pmd_results' folder.")

# Script PowerShell pour générer la documentation Javadoc

Write-Host "Génération de la documentation Javadoc..." -ForegroundColor Cyan

# Se placer dans le répertoire back
Set-Location $PSScriptRoot

# Utiliser Maven pour générer la Javadoc
mvn javadoc:javadoc

if ($LASTEXITCODE -eq 0) {
    Write-Host "Documentation générée avec succès !" -ForegroundColor Green
    Write-Host ""
    Write-Host "Documentation disponible dans :" -ForegroundColor Yellow
    Write-Host "   target\site\apidocs\index.html" -ForegroundColor White
    Write-Host ""
    
    # Ouvrir automatiquement la documentation
    Start-Process "target\site\apidocs\index.html"
}
else {
    Write-Host "Erreur lors de la génération de la documentation" -ForegroundColor Red
}

Add-Type -AssemblyName System.Drawing

$resDir = "C:\Users\kavi_rawat\CodeMindStudio\TomatoProjects\TomatoDEMOApp\androidApp\src\main\res"

$densities = @{
    "mipmap-mdpi"    = 48
    "mipmap-hdpi"    = 72
    "mipmap-xhdpi"   = 96
    "mipmap-xxhdpi"  = 144
    "mipmap-xxxhdpi" = 192
}

foreach ($folder in $densities.Keys) {
    $size = $densities[$folder]
    $dirPath = Join-Path $resDir $folder

    if (!(Test-Path $dirPath)) {
        New-Item -ItemType Directory -Path $dirPath -Force | Out-Null
    }

    # 1. Square Icon (ic_launcher.png) with rounded corners
    $bmpSquare = New-Object System.Drawing.Bitmap $size, $size
    $gSquare = [System.Drawing.Graphics]::FromImage($bmpSquare)
    $gSquare.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
    $gSquare.Clear([System.Drawing.Color]::Transparent)

    $radius = [int]($size * 0.22)
    $diameter = $radius * 2
    $path = New-Object System.Drawing.Drawing2D.GraphicsPath
    $path.AddArc(0, 0, $diameter, $diameter, 180, 90)
    $path.AddArc($size - $diameter, 0, $diameter, $diameter, 270, 90)
    $path.AddArc($size - $diameter, $size - $diameter, $diameter, $diameter, 0, 90)
    $path.AddArc(0, $size - $diameter, $diameter, $diameter, 90, 90)
    $path.CloseFigure()

    $redBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.ColorTranslator]::FromHtml("#E23744"))
    $gSquare.FillPath($redBrush, $path)

    # Draw White Italic 't'
    $fontSize = [float]($size * 0.62)
    $font = New-Object System.Drawing.Font ("Arial", $fontSize, ([System.Drawing.FontStyle]::Bold -bor [System.Drawing.FontStyle]::Italic))
    $whiteBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::White)
    $format = New-Object System.Drawing.StringFormat
    $format.Alignment = [System.Drawing.StringAlignment]::Center
    $format.LineAlignment = [System.Drawing.StringAlignment]::Center

    $rect = New-Object System.Drawing.RectangleF 0, 0, $size, $size
    $gSquare.DrawString("t", $font, $whiteBrush, $rect, $format)

    $squarePath = Join-Path $dirPath "ic_launcher.png"
    $bmpSquare.Save($squarePath, [System.Drawing.Imaging.ImageFormat]::Png)

    $gSquare.Dispose()
    $bmpSquare.Dispose()

    # 2. Round Icon (ic_launcher_round.png)
    $bmpRound = New-Object System.Drawing.Bitmap $size, $size
    $gRound = [System.Drawing.Graphics]::FromImage($bmpRound)
    $gRound.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
    $gRound.Clear([System.Drawing.Color]::Transparent)

    $roundPath = New-Object System.Drawing.Drawing2D.GraphicsPath
    $roundPath.AddEllipse(0, 0, $size, $size)
    $gRound.FillPath($redBrush, $roundPath)

    $gRound.DrawString("t", $font, $whiteBrush, $rect, $format)

    $roundFilePath = Join-Path $dirPath "ic_launcher_round.png"
    $bmpRound.Save($roundFilePath, [System.Drawing.Imaging.ImageFormat]::Png)

    $gRound.Dispose()
    $bmpRound.Dispose()

    $font.Dispose()
    $whiteBrush.Dispose()
    $redBrush.Dispose()

    Write-Host "Generated $folder icons at $size x $size px"
}

document.addEventListener('DOMContentLoaded', () => {
    // Initial map setup
    const defaultLat = 13.0827;
    const defaultLng = 80.2757;
    
    const map = L.map('map').setView([defaultLat, defaultLng], 14);
    
    // Dark Matter Map Tiles
    L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
        subdomains: 'abcd',
        maxZoom: 19
    }).addTo(map);

    let insarLayerGroup = L.layerGroup().addTo(map);
    let targetMarker = null;
    let radiusCircle = null;

    // DOM Elements
    const assetSelect = document.getElementById('asset-select');
    const inputLat = document.getElementById('input-lat');
    const inputLng = document.getElementById('input-lng');
    const inputRadius = document.getElementById('input-radius');
    const inputType = document.getElementById('input-type');
    const btnAnalyze = document.getElementById('btn-analyze');

    const metricCount = document.getElementById('metric-count');
    const metricAvg = document.getElementById('metric-avg');
    const metricMin = document.getElementById('metric-min');
    const metricNeg = document.getElementById('metric-neg');

    const riskScoreRing = document.getElementById('risk-score-ring');
    const riskScoreNum = document.getElementById('risk-score-num');
    const riskLevelBadge = document.getElementById('risk-level-badge');
    const riskSummary = document.getElementById('risk-summary');
    const factorsList = document.getElementById('factors-list');
    const ragExplanationBody = document.getElementById('rag-explanation-body');

    let loadedAssets = [];

    // 1. Fetch available preset assets
    fetch('/api/infrastructure')
        .then(res => res.json())
        .then(assets => {
            loadedAssets = assets;
            assets.forEach(asset => {
                const opt = document.createElement('option');
                opt.value = asset.id;
                opt.textContent = `${asset.name} (${asset.type})`;
                assetSelect.appendChild(opt);
            });
            // Automatically select first asset
            if (assets.length > 0) {
                assetSelect.value = assets[0].id;
                onAssetSelected(assets[0].id);
            }
        })
        .catch(err => console.error('Error loading preset infrastructure assets:', err));

    // Handle Asset dropdown selection
    assetSelect.addEventListener('change', (e) => {
        const id = e.target.value;
        if (id) {
            onAssetSelected(id);
        }
    });

    function onAssetSelected(id) {
        const asset = loadedAssets.find(a => a.id == id);
        if (asset) {
            inputLat.value = asset.latitude;
            inputLng.value = asset.longitude;
            inputType.value = asset.type;
            runAnalysis(asset.latitude, asset.longitude, inputRadius.value, asset.name, asset.type, asset.id);
        }
    }

    btnAnalyze.addEventListener('click', () => {
        const lat = parseFloat(inputLat.value);
        const lng = parseFloat(inputLng.value);
        const radius = parseFloat(inputRadius.value);
        const type = inputType.value;
        const selectedOpt = assetSelect.options[assetSelect.selectedIndex];
        const name = selectedOpt && selectedOpt.value ? selectedOpt.text.split(' (')[0] : 'Selected Coordinate';

        const assetId = assetSelect.value || null;
        runAnalysis(lat, lng, radius, name, type, assetId);
    });

    function runAnalysis(lat, lng, radius, name, type, assetId) {
        // Update map view & marker
        map.setView([lat, lng], 15);

        if (targetMarker) map.removeLayer(targetMarker);
        if (radiusCircle) map.removeLayer(radiusCircle);

        targetMarker = L.marker([lat, lng], {
            title: name
        }).addTo(map).bindPopup(`<b>${name}</b><br>Type: ${type}`).openPopup();

        radiusCircle = L.circle([lat, lng], {
            radius: radius,
            color: '#6366F1',
            fillColor: '#6366F1',
            fillOpacity: 0.1,
            weight: 2
        }).addTo(map);

        // Fetch spatial InSAR scatter points
        fetch(`/api/insar/nearby?latitude=${lat}&longitude=${lng}&radius=${radius}`)
            .then(res => res.json())
            .then(points => renderInsarPoints(points))
            .catch(err => console.error('Error fetching nearby InSAR measurements:', err));

        // Fetch Risk & RAG explanation
        const riskUrl = assetId
            ? `/api/infrastructure/${assetId}/risk?radius=${radius}`
            : `/api/assessment/evaluate?latitude=${lat}&longitude=${lng}&radius=${radius}&name=${encodeURIComponent(name)}&type=${type}`;

        fetch(riskUrl)
            .then(res => res.json())
            .then(data => renderRiskData(data))
            .catch(err => console.error('Error fetching risk analysis:', err));
    }

    function renderInsarPoints(points) {
        insarLayerGroup.clearLayers();

        points.forEach(pt => {
            const vlm = pt.vlmMmPerYear;
            let color = '#10B981'; // Green (Stable/Uplift)
            if (vlm <= -5.0) {
                color = '#EF4444'; // Severe red
            } else if (vlm <= -2.0) {
                color = '#F59E0B'; // Orange
            } else if (vlm < 0) {
                color = '#FBBF24'; // Mild yellow
            }

            const circle = L.circleMarker([pt.latitude, pt.longitude], {
                radius: 4,
                fillColor: color,
                color: color,
                weight: 1,
                opacity: 0.8,
                fillOpacity: 0.7
            });

            circle.bindTooltip(`VLM: ${vlm.toFixed(2)} mm/yr`, { permanent: false });
            insarLayerGroup.addLayer(circle);
        });
    }

    function renderRiskData(data) {
        const analysis = data.analysis;
        const risk = data.risk;

        // Metrics update
        metricCount.textContent = analysis.measurementCount;
        metricAvg.textContent = `${analysis.averageVlmMmPerYear} mm/yr`;
        metricMin.textContent = `${analysis.minimumVlmMmPerYear} mm/yr`;
        metricNeg.textContent = `${analysis.negativeMovementPercentage}%`;

        // Risk Gauge & Level
        riskScoreNum.textContent = risk.riskScore;
        riskLevelBadge.textContent = risk.riskLevel;
        riskLevelBadge.className = `risk-level-badge ${risk.riskLevel}`;

        let ringColor = '#10B981';
        if (risk.riskLevel === 'HIGH') ringColor = '#EF4444';
        else if (risk.riskLevel === 'MEDIUM') ringColor = '#F59E0B';

        riskScoreRing.style.borderColor = ringColor;
        riskScoreRing.style.boxShadow = `0 0 25px ${ringColor}`;

        riskSummary.textContent = risk.summary;

        // Factors List
        factorsList.innerHTML = '';
        if (risk.riskFactors && risk.riskFactors.length > 0) {
            risk.riskFactors.forEach(factor => {
                const item = document.createElement('div');
                item.className = `factor-item ${factor.severity}`;
                item.innerHTML = `
                    <span class="factor-desc"><strong>${factor.category}:</strong> ${factor.description}</span>
                    <span class="factor-pts">+${factor.impactPoints} pts</span>
                `;
                factorsList.appendChild(item);
            });
        } else {
            factorsList.innerHTML = '<div class="placeholder-text">No significant risk drivers detected.</div>';
        }

        // RAG Explanation
        if (data.explanation) {
            ragExplanationBody.innerHTML = window.marked ? marked.parse(data.explanation) : data.explanation;
        } else {
            ragExplanationBody.innerHTML = '<div class="placeholder-text">Explanation unavailable for this query.</div>';
        }
    }
});

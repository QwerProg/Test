document.addEventListener('DOMContentLoaded', () => {
    const playerTagInput = document.getElementById('playerTagInput');
    const fetchButton = document.getElementById('fetchButton');
    const loadingIndicator = document.getElementById('loadingIndicator');
    const errorDisplay = document.getElementById('errorDisplay');
    const playerDataDisplay = document.getElementById('playerDataDisplay');

    fetchButton.addEventListener('click', async () => {
        const playerTag = playerTagInput.value.trim();
        if (!playerTag) {
            showError("请输入玩家标签！");
            return;
        }
        if (!playerTag.startsWith('#')) {
            showError("玩家标签必须以 # 开头。");
            return;
        }


        // 清理旧数据和错误
        playerDataDisplay.innerHTML = '';
        errorDisplay.style.display = 'none';
        loadingIndicator.style.display = 'block';

        try {
            // URL编码玩家标签, Spring Boot Controller会自动解码路径变量中的%23
            const encodedPlayerTag = encodeURIComponent(playerTag);
            const response = await fetch(`/api/v1/players/${encodedPlayerTag}`);

            loadingIndicator.style.display = 'none';

            if (!response.ok) {
                const errorData = await response.text(); // 或者 response.json() 如果后端返回JSON错误体
                throw new Error(`错误 ${response.status}: ${errorData || response.statusText}`);
            }

            const playerData = await response.json();
            displayPlayerData(playerData);

        } catch (error) {
            loadingIndicator.style.display = 'none';
            showError(error.message);
            console.error('获取玩家数据失败:', error);
        }
    });

    function showError(message) {
        errorDisplay.textContent = message;
        errorDisplay.style.display = 'block';
    }

    function displayPlayerData(data) {
        if (!data) {
            showError("未能获取到有效的玩家数据。");
            return;
        }

        let html = '<h2>基本信息</h2>';
        html += `<p><strong>名称:</strong> ${data.name || 'N/A'}</p>`;
        html += `<p><strong>标签:</strong> ${data.tag || 'N/A'}</p>`;
        html += `<p><strong>大本营等级:</strong> ${data.townHallLevel || 'N/A'} (武器等级: ${data.townHallWeaponLevel || 'N/A'})</p>`;
        html += `<p><strong>经验等级:</strong> ${data.expLevel || 'N/A'}</p>`;
        html += `<p><strong>奖杯:</strong> ${data.trophies || 'N/A'} (最高: ${data.bestTrophies || 'N/A'})</p>`;
        html += `<p><strong>战争星数:</strong> ${data.warStars || 'N/A'}</p>`;
        html += `<p><strong>捐兵:</strong> ${data.donations || 0} | <strong>收兵:</strong> ${data.donationsReceived || 0}</p>`;


        if (data.clan) {
            html += `<h2>部落信息</h2>`;
            html += `<p><strong>名称:</strong> ${data.clan.name || 'N/A'} (等级: ${data.clan.clanLevel || 'N/A'})</p>`;
            html += `<p><strong>标签:</strong> ${data.clan.tag || 'N/A'}</p>`;
            if (data.clan.badgeUrls && data.clan.badgeUrls.small) {
                html += `<p><img src="${data.clan.badgeUrls.small}" alt="部落徽章" class="badge"></p>`;
            }
            html += `<p><strong>你的角色:</strong> ${data.role || 'N/A'}</p>`;
        }

        if (data.league) {
            html += `<h2>联赛信息</h2>`;
            html += `<p><strong>联赛:</strong> ${data.league.name || 'N/A'}`;
            if (data.league.iconUrls && data.league.iconUrls.small) {
                html += ` <img src="${data.league.iconUrls.small}" alt="联赛图标" class="badge">`;
            }
            html += `</p>`;
        }

        if (data.legendStatistics && data.legendStatistics.currentSeason) {
            html += `<h2>传奇杯 (${data.legendStatistics.legendTrophies || 'N/A'})</h2>`;
            html += `<p><strong>当前赛季排名:</strong> ${data.legendStatistics.currentSeason.rank || 'N/A'}</p>`;
            html += `<p><strong>当前赛季奖杯:</strong> ${data.legendStatistics.currentSeason.trophies || 'N/A'}</p>`;
        }


        if (data.heroes && data.heroes.length > 0) {
            html += `<h2>英雄 (主村庄)</h2><ul>`;
            data.heroes.filter(h => h.village === 'home').slice(0, 5).forEach(hero => { // 最多显示5个
                let equipmentInfo = '';
                if (hero.equipment && hero.equipment.length > 0) {
                    equipmentInfo = ` (装备: ${hero.equipment.map(eq => `${eq.name} Lv${eq.level}`).join(', ')})`;
                }
                html += `<li><strong>${hero.name}:</strong> 等级 <span class="math-inline">\{hero\.level\}/</span>{hero.maxLevel}${equipmentInfo}</li>`;
            });
            html += `</ul>`;
        }

        if (data.troops && data.troops.length > 0) {
            html += `<h2>兵种 (主村庄，部分)</h2><ul>`;
            data.troops.filter(t => t.village === 'home' && t.level > 0).slice(0,7).forEach(troop => { // 最多显示7个
                html += `<li><strong>${troop.name}:</strong> 等级 <span class="math-inline">\{troop\.level\}/</span>{troop.maxLevel}</li>`;
            });
            html += `</ul>`;
        }

        if (data.spells && data.spells.length > 0) {
            html += `<h2>法术 (部分)</h2><ul>`;
            data.spells.filter(s => s.village === 'home' && s.level > 0).slice(0,5).forEach(spell => { // 最多显示5个
                html += `<li><strong>${spell.name}:</strong> 等级 <span class="math-inline">\{spell\.level\}/</span>{spell.maxLevel}</li>`;
            });
            html += `</ul>`;
        }

        playerDataDisplay.innerHTML = html;
    }
});
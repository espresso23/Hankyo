class VideoPlayer {
    constructor(containerId) {
        this.container = document.getElementById(containerId);
        this.video = this.container.querySelector('.video-player');
        this.playOverlay = this.container.querySelector('.video-overlay');
        this.playButton = this.container.querySelector('.video-play-button');
        this.playPauseBtn = this.container.querySelector('.play-pause');
        this.centerPlayPause = this.container.querySelector('.center-play-pause');
        this.rewindBtn = this.container.querySelector('.rewind-10s');
        this.forwardBtn = this.container.querySelector('.forward-10s');
        this.volumeBtn = this.container.querySelector('.volume');
        this.progress = this.container.querySelector('.video-progress');
        this.progressBar = this.container.querySelector('.video-progress-filled');
        this.progressBuffer = this.container.querySelector('.video-progress-buffer');
        this.currentTime = this.container.querySelector('.current-time');
        this.duration = this.container.querySelector('.duration');
        this.speedToggle = this.container.querySelector('.speed-toggle');
        this.speedLabel = this.container.querySelector('.speed-label');
        this.speedMenu = this.container.querySelector('.speed-menu');
        this.fullscreenBtn = this.container.querySelector('.fullscreen');
        this.volumeSlider = this.container.querySelector('.volume-slider');
        this.volumeBar = this.container.querySelector('.volume-slider-bar');
        this.volumeFill = this.container.querySelector('.volume-slider-fill');
        this.volumeHandle = this.container.querySelector('.volume-slider-handle');
        this.volumeTooltip = this.container.querySelector('.volume-tooltip');

        this.isPlaying = false;
        this.isMuted = false;
        this.currentSpeed = 1;
        this.isMouseDown = false;
        this.isControlsVisible = true;
        this.controlsTimeout = null;
        this.isVolumeSliding = false;
        this.currentVolume = 1;
        this.lastVolume = 1;

        // Khởi tạo volume UI
        if (this.video) {
            this.video.volume = this.currentVolume;
            this.updateVolumeUI();
        }

        // Force show controls initially
        this.showControls();
        this.container.classList.remove('hide-controls');

        // Add click event for play button
        if (this.playButton) {
            this.playButton.addEventListener('click', () => {
                this.playVideo();
            });
        }

        this.setupEventListeners();
        this.initializeControls();
    }

    setupEventListeners() {
        // Video overlay play button
        if (this.playButton) {
            this.playButton.addEventListener('click', () => {
                this.playVideo();
            });
        }

        // Video click
        if (this.video) {
            this.video.addEventListener('click', () => {
                this.togglePlay();
            });
        }

        // Play/Pause button
        if (this.playPauseBtn) {
            this.playPauseBtn.addEventListener('click', () => {
                this.togglePlay();
            });
        }

        // Volume control events
        if (this.volumeBar) {
            this.volumeBar.addEventListener('mousedown', (e) => {
                e.stopPropagation();
                this.isVolumeSliding = true;
                this.updateVolume(e);
                
                // Nếu đang mute, unmute khi điều chỉnh volume
                if (this.video.muted) {
                    this.video.muted = false;
                    this.updateVolumeUI();
                }
            });

            this.volumeBar.addEventListener('mousemove', (e) => {
                if (this.isVolumeSliding) {
                    this.updateVolume(e);
                }
                this.updateVolumeTooltip(e);
            });

            this.volumeBar.addEventListener('mouseenter', (e) => {
                this.updateVolumeTooltip(e);
            });

            document.addEventListener('mouseup', () => {
                this.isVolumeSliding = false;
            });

            document.addEventListener('mousemove', (e) => {
                if (this.isVolumeSliding) {
                    this.updateVolume(e);
                }
            });
        }

        // Volume button
        if (this.volumeBtn) {
            this.volumeBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                this.toggleMute();
            });
        }

        // Progress bar events
        if (this.progress) {
            this.progress.addEventListener('click', (e) => this.scrub(e));
            this.progress.addEventListener('mousemove', (e) => {
                this.updateSeekPreview(e);
                if (this.isMouseDown) this.scrub(e);
            });
            this.progress.addEventListener('mousedown', () => this.isMouseDown = true);
            this.progress.addEventListener('mouseup', () => this.isMouseDown = false);
            this.progress.addEventListener('mouseleave', () => {
                const preview = this.progress.querySelector('.video-seek-preview');
                if (preview) preview.remove();
            });
        }

        // Playback speed
        if (this.speedToggle) {
            this.speedToggle.addEventListener('click', () => {
                this.speedMenu.classList.toggle('active');
            });

            this.container.querySelectorAll('.speed-item').forEach(option => {
                option.addEventListener('click', (e) => {
                    const speed = parseFloat(e.target.dataset.speed);
                    this.changePlaybackSpeed(speed);
                    this.speedMenu.classList.remove('active');
                });
            });
        }

        // Fullscreen
        if (this.fullscreenBtn) {
            this.fullscreenBtn.addEventListener('click', () => this.toggleFullscreen());
        }

        // Rewind 10 seconds
        if (this.rewindBtn) {
            this.rewindBtn.addEventListener('click', () => {
                this.seekRelative(-10);
            });
        }

        // Forward 10 seconds
        if (this.forwardBtn) {
            this.forwardBtn.addEventListener('click', () => {
                this.seekRelative(10);
            });
        }

        // Keyboard controls
        document.addEventListener('keydown', (e) => {
            if (this.container.contains(e.target)) {
                this.handleKeypress(e);
            }
        });

        // Close menus when clicking outside
        document.addEventListener('click', (e) => {
            if (this.speedMenu && !this.speedToggle.contains(e.target)) {
                this.speedMenu.classList.remove('active');
            }
        });

        // Video events
        if (this.video) {
            this.video.addEventListener('timeupdate', () => this.updateProgress());
            this.video.addEventListener('loadedmetadata', () => this.updateDuration());
            this.video.addEventListener('progress', () => this.updateBuffer());
            this.video.addEventListener('play', () => this.onPlay());
            this.video.addEventListener('pause', () => this.onPause());
            this.video.addEventListener('ended', () => this.onEnded());
            this.video.addEventListener('loadedmetadata', () => {
                this.updateVolumeUI();
            });
        }

        // Container hover
        this.container.addEventListener('mousemove', () => {
            this.showControls();
            if (this.isPlaying) {
                this.hideControlsWithDelay();
            }
        });

        this.container.addEventListener('mouseleave', () => {
            if (this.isPlaying && !this.isMouseDown) {
                this.hideControls();
            }
        });

        // Mouse enter vào video container
        this.container.addEventListener('mouseenter', () => {
            this.showControls();
            if (this.isPlaying) {
                this.hideControlsWithDelay();
            }
        });
    }

    initializeControls() {
        this.showControls();
        if (this.isPlaying) {
            this.hideControlsWithDelay();
        }
    }

    showControls() {
        this.isControlsVisible = true;
        this.container.classList.remove('hide-controls');
        const controlsContainer = this.container.querySelector('.video-controls-container');
        if (controlsContainer) {
            controlsContainer.style.opacity = '1';
            controlsContainer.style.visibility = 'visible';
            controlsContainer.style.transform = 'translateY(0)';
        }
        clearTimeout(this.controlsTimeout);
    }

    hideControls() {
        if (this.isPlaying) {
            this.isControlsVisible = false;
            this.container.classList.add('hide-controls');
            const controlsContainer = this.container.querySelector('.video-controls-container');
            if (controlsContainer) {
                controlsContainer.style.opacity = '0';
                controlsContainer.style.visibility = 'hidden';
                controlsContainer.style.transform = 'translateY(100%)';
            }
        }
    }

    hideControlsWithDelay() {
        if (this.isPlaying) {
            clearTimeout(this.controlsTimeout);
            this.controlsTimeout = setTimeout(() => {
                if (this.isPlaying && !this.container.matches(':hover')) {
                    this.hideControls();
                }
            }, 2000);
        }
    }

    playVideo() {
        if (this.video) {
            this.video.play();
            this.isPlaying = true;
            this.container.classList.add('playing');
            if (this.playOverlay) {
                this.playOverlay.style.opacity = '0';
                this.playOverlay.style.visibility = 'hidden';
            }
            this.updatePlayButton();
            this.hideControlsWithDelay();
        }
    }

    pauseVideo() {
        if (this.video) {
            this.video.pause();
            this.isPlaying = false;
            this.container.classList.remove('playing');
            if (this.playOverlay) {
                this.playOverlay.style.opacity = '1';
                this.playOverlay.style.visibility = 'visible';
            }
            this.updatePlayButton();
            this.showControls();
        }
    }

    togglePlay() {
        if (this.isPlaying) {
            this.pauseVideo();
        } else {
            this.playVideo();
        }
        this.showCenterPlayPause();
    }

    showCenterPlayPause() {
        if (this.centerPlayPause) {
            // Cập nhật icon
            this.centerPlayPause.querySelector('i').className = 
                this.isPlaying ? 'fas fa-pause' : 'fas fa-play';
            
            // Reset animation bằng cách xóa và thêm lại class
            this.centerPlayPause.classList.remove('show');
            void this.centerPlayPause.offsetWidth; // Force reflow
            this.centerPlayPause.classList.add('show');
        }
    }

    handleKeypress(e) {
        switch(e.key.toLowerCase()) {
            case ' ':
            case 'k':
                e.preventDefault();
                this.togglePlay();
                break;
            case 'm':
                this.toggleMute();
                break;
            case 'f':
                this.toggleFullscreen();
                break;
            case 'arrowleft':
                this.seekRelative(-10);
                break;
            case 'arrowright':
                this.seekRelative(10);
                break;
            case 'arrowup':
                e.preventDefault();
                this.changeVolume(0.1);
                break;
            case 'arrowdown':
                e.preventDefault();
                this.changeVolume(-0.1);
                break;
        }
    }

    seekRelative(seconds) {
        if (this.video) {
            const newTime = Math.max(0, Math.min(this.video.duration, this.video.currentTime + seconds));
            this.video.currentTime = newTime;
            
            const feedback = document.createElement('div');
            feedback.className = 'seek-feedback';
            feedback.innerHTML = `<i class="fas fa-${seconds > 0 ? 'forward' : 'backward'}"></i> ${Math.abs(seconds)}s`;
            this.container.appendChild(feedback);
            
            setTimeout(() => {
                feedback.remove();
            }, 1000);
        }
    }

    changeVolume(delta) {
        if (this.video) {
            const newVolume = Math.max(0, Math.min(1, this.video.volume + delta));
            this.video.volume = newVolume;
            
            if (newVolume === 0) {
                this.volumeBtn.innerHTML = '<i class="fas fa-volume-mute"></i>';
            } else if (newVolume < 0.5) {
                this.volumeBtn.innerHTML = '<i class="fas fa-volume-down"></i>';
            } else {
                this.volumeBtn.innerHTML = '<i class="fas fa-volume-up"></i>';
            }
        }
    }

    changePlaybackSpeed(speed) {
        if (this.video) {
            this.currentSpeed = speed;
            this.video.playbackRate = speed;
            if (this.speedLabel) {
                this.speedLabel.textContent = speed + 'x';
            }
            
            this.container.querySelectorAll('.speed-item').forEach(option => {
                option.classList.toggle('active', parseFloat(option.dataset.speed) === speed);
            });
        }
    }

    updateProgress() {
        if (this.video && this.progressBar && this.currentTime) {
            const percentage = (this.video.currentTime / this.video.duration) * 100;
            this.progressBar.style.width = `${percentage}%`;
            this.currentTime.textContent = this.formatTime(this.video.currentTime);
        }
    }

    updateBuffer() {
        if (this.video && this.progressBuffer) {
            const buffered = this.video.buffered;
            if (buffered.length > 0) {
                const bufferedEnd = buffered.end(buffered.length - 1);
                const duration = this.video.duration;
                const percent = (bufferedEnd / duration) * 100;
                this.progressBuffer.style.width = `${percent}%`;
            }
        }
    }

    updateDuration() {
        if (this.video && this.duration) {
            this.duration.textContent = this.formatTime(this.video.duration);
        }
    }

    scrub(e) {
        if (this.video && this.progress) {
            const scrubTime = (e.offsetX / this.progress.offsetWidth) * this.video.duration;
            this.video.currentTime = scrubTime;
        }
    }

    formatTime(seconds) {
        if (isNaN(seconds)) return '0:00';
        const date = new Date(seconds * 1000);
        const mm = date.getUTCMinutes();
        const ss = date.getUTCSeconds().toString().padStart(2, '0');
        return `${mm}:${ss}`;
    }

    toggleFullscreen() {
        if (!document.fullscreenElement) {
            this.container.requestFullscreen();
            this.fullscreenBtn.innerHTML = '<i class="fas fa-compress"></i>';
        } else {
            document.exitFullscreen();
            this.fullscreenBtn.innerHTML = '<i class="fas fa-expand"></i>';
        }
    }

    toggleMute() {
        if (this.video) {
            // Toggle mute state
            this.isMuted = !this.isMuted;
            this.video.muted = this.isMuted;

            if (this.isMuted) {
                // Lưu volume hiện tại trước khi mute
                this.lastVolume = this.video.volume;
            } else {
                // Khôi phục volume khi unmute
                if (this.lastVolume > 0) {
                    this.video.volume = this.lastVolume;
                } else {
                    this.video.volume = 0.5; // Default volume nếu lastVolume = 0
                }
            }

            // Cập nhật UI
            this.updateVolumeUI();
            
            // Log để debug
            console.log('Toggle mute:', {
                isMuted: this.isMuted,
                currentVolume: this.video.volume,
                lastVolume: this.lastVolume
            });
        }
    }

    addClickRipple(element) {
        if (!element) return;
        
        // Tạo ripple effect
        const ripple = document.createElement('span');
        ripple.classList.add('ripple-effect');
        
        // Tính toán kích thước của ripple
        const diameter = Math.max(element.clientWidth, element.clientHeight);
        ripple.style.width = ripple.style.height = `${diameter}px`;
        
        // Xóa ripple cũ nếu có
        const existingRipple = element.querySelector('.ripple-effect');
        if (existingRipple) {
            existingRipple.remove();
        }
        
        // Thêm ripple mới
        element.appendChild(ripple);
        
        // Xóa ripple sau khi animation kết thúc
        setTimeout(() => {
            ripple.remove();
        }, 600);
    }

    updatePlayButton() {
        if (this.playPauseBtn) {
            this.playPauseBtn.innerHTML = this.isPlaying ? 
                '<i class="fas fa-pause"></i>' : 
                '<i class="fas fa-play"></i>';
        }
    }

    onPlay() {
        this.isPlaying = true;
        this.updatePlayButton();
        if (this.playOverlay) {
            this.playOverlay.style.display = 'none';
        }
    }

    onPause() {
        this.isPlaying = false;
        this.updatePlayButton();
    }

    onEnded() {
        this.isPlaying = false;
        this.updatePlayButton();
        if (this.playOverlay) {
            this.playOverlay.style.display = 'flex';
        }
    }

    updateSeekPreview(e) {
        if (!this.video || !this.progress) return;
        
        const rect = this.progress.getBoundingClientRect();
        const percent = Math.min(Math.max(0, e.pageX - rect.x), rect.width) / rect.width;
        const previewTime = this.video.duration * percent;
        
        const preview = this.progress.querySelector('.video-seek-preview') || document.createElement('div');
        preview.className = 'video-seek-preview';
        preview.textContent = this.formatTime(previewTime);
        preview.style.left = `${e.pageX - rect.x}px`;
        
        if (!preview.parentElement) {
            this.progress.appendChild(preview);
        }
    }

    updateVolume(e) {
        if (this.video && this.volumeBar) {
            const rect = this.volumeBar.getBoundingClientRect();
            let volume = Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width));
            
            // Nếu đang mute, unmute khi điều chỉnh volume
            if (this.video.muted) {
                this.video.muted = false;
                this.isMuted = false;
            }
            
            this.video.volume = volume;
            this.currentVolume = volume;
            this.lastVolume = volume;
            
            this.updateVolumeUI();
            
            // Log để debug
            console.log('Volume updated:', {
                volume: volume,
                currentVolume: this.currentVolume,
                lastVolume: this.lastVolume
            });
        }
    }

    updateVolumeUI() {
        if (this.volumeFill && this.volumeHandle) {
            const percentage = this.video.muted ? 0 : (this.video.volume * 100);
            this.volumeFill.style.width = `${percentage}%`;
            this.volumeHandle.style.left = `${percentage}%`;
            
            // Cập nhật tooltip
            if (this.volumeTooltip) {
                this.volumeTooltip.textContent = `${Math.round(percentage)}%`;
            }
            
            // Cập nhật icon và class
            if (this.volumeBtn) {
                if (this.video.muted || this.video.volume === 0) {
                    this.volumeBtn.innerHTML = '<i class="fas fa-volume-mute"></i>';
                    this.volumeBtn.classList.add('muted');
                } else if (this.video.volume < 0.5) {
                    this.volumeBtn.innerHTML = '<i class="fas fa-volume-down"></i>';
                    this.volumeBtn.classList.remove('muted');
                } else {
                    this.volumeBtn.innerHTML = '<i class="fas fa-volume-up"></i>';
                    this.volumeBtn.classList.remove('muted');
                }
            }

            // Log để debug
            console.log('Volume UI updated:', {
                percentage: percentage,
                isMuted: this.video.muted,
                volume: this.video.volume
            });
        }
    }

    updateVolumeTooltip(e) {
        if (this.volumeBar && this.volumeTooltip) {
            const rect = this.volumeBar.getBoundingClientRect();
            const volume = Math.max(0, Math.min(1, (e.clientX - rect.left) / rect.width));
            const percentage = Math.round(volume * 100);
            this.volumeTooltip.textContent = `${percentage}%`;
        }
    }
}

// Initialize video player when document is ready
document.addEventListener('DOMContentLoaded', () => {
    const videoContainer = document.getElementById('videoContainer');
    if (videoContainer) {
        new VideoPlayer('videoContainer');
    }
}); 
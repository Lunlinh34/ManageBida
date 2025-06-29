let sparkleCount = 0;

function createSparkle() {
    if (sparkleCount >= 20) return;

    const sparkle = document.createElement('div');
    sparkle.classList.add('sparkle');
    sparkleCount++;

    const container = document.querySelector('.collection-container');
    const x = Math.random() * container.offsetWidth;
    const y = Math.random() * container.offsetHeight;

    sparkle.style.left = `${x}px`;
    sparkle.style.top = `${y}px`;

    container.appendChild(sparkle);

    const size = Math.random() * 6 + 4;
    sparkle.style.width = `${size}px`;
    sparkle.style.height = `${size}px`;

    setTimeout(() => {
        sparkle.style.opacity = Math.random() * 0.7 + 0.3;

        setTimeout(() => {
            sparkle.style.opacity = 0;

            setTimeout(() => {
                sparkle.remove();
                sparkleCount--;
                createSparkle();
            }, 500);
        }, Math.random() * 1500 + 500);
    }, 200);

    setTimeout(createSparkle, Math.random() * 800 + 200);
}

window.addEventListener('DOMContentLoaded', () => {
    for (let i = 0; i < 5; i++) {
        setTimeout(() => createSparkle(), i * 300);
    }
});

function createSparkle() {
    const sparkle = document.createElement('div');
    sparkle.classList.add('sparkle');

    const container = document.querySelector('.collection-container');
    const x = Math.random() * container.offsetWidth;
    const y = Math.random() * container.offsetHeight;

    sparkle.style.left = `${x}px`;
    sparkle.style.top = `${y}px`;

    container.appendChild(sparkle);

    gsap.to(sparkle, {
        opacity: Math.random() * 0.7 + 0.3,
        duration: 0.2,
        onComplete: () => {
            gsap.to(sparkle, {
                opacity: 0,
                duration: 0.5,
                delay: Math.random() * 1.5,
                onComplete: () => {
                    sparkle.remove();
                }
            });
        }
    });

    setTimeout(createSparkle, Math.random() * 800 + 200);
}

window.addEventListener('DOMContentLoaded', () => {
    if (typeof gsap === 'undefined') {
        const script = document.createElement('script');
        script.src = 'https://cdnjs.cloudflare.com/ajax/libs/gsap/3.11.4/gsap.min.js';
        script.onload = createSparkle;
        document.head.appendChild(script);
    } else {
        createSparkle();
    }
    const observer = new IntersectionObserver(
        (entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate');
                    observer.unobserve(entry.target);
                }
            });
        },
        { threshold: 0.2 }
    );

    observer.observe(document.querySelector('.collection-container'));
});

document.addEventListener('DOMContentLoaded', function () {
    const observer = new IntersectionObserver(
        (entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const elements = entry.target.querySelectorAll('.product-card, .testimonial-card');
                    elements.forEach(el => {
                        el.style.animationPlayState = 'running';
                    });
                    observer.unobserve(entry.target);
                }
            });
        },
        { threshold: 0.2 }
    );

    observer.observe(document.querySelector('.product-showcase'));
    observer.observe(document.querySelector('.testimonials-section'));

    const productCards = document.querySelectorAll('.product-card');
    productCards.forEach(card => {
        card.addEventListener('mouseenter', function () {
            if (typeof gsap !== 'undefined') {
                gsap.to(this.querySelector('.product-image img'), {
                    scale: 1.05,
                    duration: 0.5,
                    ease: 'power2.out'
                });
            }
        });

        card.addEventListener('mouseleave', function () {
            if (typeof gsap !== 'undefined') {
                gsap.to(this.querySelector('.product-image img'), {
                    scale: 1,
                    duration: 0.5,
                    ease: 'power2.out'
                });
            }
        });
    });
});

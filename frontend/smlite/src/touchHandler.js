document.addEventListener('touchstart', handleTouchStart, false);
document.addEventListener('touchmove', handleTouchMove, false);

let xDown = null;
let currentPage = 3;

function getTouches(evt) {
  return evt.touches || evt.originalEvent.touches;
}

function handleTouchStart(evt) {
  const firstTouch = getTouches(evt)[0];
  xDown = firstTouch.clientX;
}

function handleTouchMove(evt) {
  if (!xDown)
    return;


  let xUp = evt.touches[0].clientX;

  let xDiff = xDown - xUp;

  if (xDiff > 0) {
    console.log("left");
    if (currentPage < 5) {
      setPage(currentPage, currentPage + 1);
      currentPage = currentPage + 1;
    }
  } else {
    console.log("right");
    if (currentPage > 1) {
      setPage(currentPage, currentPage - 1);
      currentPage = currentPage - 1;
    }
  }

  xDown = null;
}

function goToPage(page) {
  if (page !== currentPage) {
    setPage(currentPage, page);
    currentPage = page;
  }
}

function setPage(prevPage, page) {
  $('#item' + page).css("transform", "translateY(-2vh)");
  $('#item' + prevPage).css("transform", "translateY(0)");
  if (page > prevPage) {
    $("#page" + page + ", #page" + prevPage).css({transform: "scale(0.9)"});
    $("#page" + page).animate({left: "0vw"}, 100, function () {
      $(this).css({transform: ""})
    });
    $("#page" + prevPage).animate({left: "-100vw"}, 100, function () {
      $(this).css({transform: ""})
    });
  } else {
    $("#page" + page + ", #page" + prevPage).css({transform: "scale(0.9)"});
    $("#page" + page).animate({left: "0vw"}, 100, function () {
      $(this).css({transform: ""})
    });
    $("#page" + prevPage).animate({left: "100vw"}, 100, function () {
      $(this).css({transform: ""})
    });
  }
}
